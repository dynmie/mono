package me.dynmie.mono.server.network.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.dynmie.mono.server.client.ClientService;
import me.dynmie.mono.server.client.session.SessionService;
import me.dynmie.mono.server.data.ServerConfig;
import me.dynmie.mono.server.network.connection.ConnectionService;

import java.net.InetAddress;
import java.util.logging.Logger;

/**
 * @author dynmie
 */
public class NetworkHandler {

    private final Logger logger;
    private final ServerConfig.NetworkInformation networkInformation;
    private final ConnectionService connectionService;
    private final SessionService sessionService;
    private final ClientService clientService;

    private Channel serverChannel;


    public NetworkHandler(Logger logger, ServerConfig.NetworkInformation networkInformation, ConnectionService connectionService, SessionService sessionService, ClientService clientService) {
        this.logger = logger;
        this.networkInformation = networkInformation;
        this.connectionService = connectionService;
        this.sessionService = sessionService;
        this.clientService = clientService;
    }

    public void start() {
        InetAddress address = networkInformation.getAddress();
        int port = networkInformation.getPort();

        EventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        channel.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(2097152));
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new PacketDecoder(connectionService, sessionService, clientService, logger));
                        pipeline.addLast(new SerializableEncoder());
                    }
                });

        ChannelFuture bind = bootstrap.bind(address, port)
                .syncUninterruptibly()
                .awaitUninterruptibly();

        if (bind.isSuccess()) {
            logger.info("Successfully bound to port " + port + ".");
        } else {
            logger.severe("Failed to bind to port " + port + "; is it in use by another application?");
        }

        serverChannel = bind.channel();
    }

    public void shutdown() {
        if (serverChannel != null) {
            try {
                serverChannel.close().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to close channel.", e);
            }
        }
    }
}
