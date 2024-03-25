package me.dynmie.mono.client.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.Setter;
import me.dynmie.mono.client.QClient;
import me.dynmie.mono.client.data.ClientConfig;
import me.dynmie.mono.client.network.connection.ServerConnection;
import me.dynmie.mono.client.network.netty.PacketDecoder;
import me.dynmie.mono.client.network.netty.SerializableEncoder;
import me.dynmie.mono.shared.packet.handshake.server.ServerboundHandshakePacket;
import me.dynmie.mono.shared.session.ClientSession;

import java.net.InetAddress;
import java.util.logging.Logger;

/**
 * @author dynmie
 */
public class NetworkHandler {

    private final QClient client;
    private final Logger logger;
    private final ClientConfig.NetworkInformation networkInformation;

    private @Getter ServerConnection connection;
    private @Getter @Setter ClientSession session;

    public NetworkHandler(QClient client, Logger logger, ClientConfig.NetworkInformation networkInformation) {
        this.client = client;
        this.logger = logger;
        this.networkInformation = networkInformation;
    }

    public void start() {
        InetAddress address = networkInformation.getAddress();
        int port = networkInformation.getPort();
        logger.info("Attempting to connect to the server on port " + port + "...");

        EventLoopGroup group = new NioEventLoopGroup();

        NetworkHandler networkHandler = this;
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        channel.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(2097152));
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new PacketDecoder(networkHandler));
                        pipeline.addLast(new SerializableEncoder());
                    }
                });

        ChannelFuture future = bootstrap.connect(address, port)
                .syncUninterruptibly()
                .awaitUninterruptibly();

        Channel channel;
        if (future.isSuccess()) {
            channel = future.channel();
            logger.info("Connection established!");
        } else {
            logger.severe("Failed to connect to the server! The client will now shut down.");
            client.shutdown();
            return;
        }

        connection = new ServerConnection(client.getInjector(), logger, channel);

        connection.sendPacket(new ServerboundHandshakePacket());
    }

    public void shutdown() {
        connection.closeChannel();
    }

}
