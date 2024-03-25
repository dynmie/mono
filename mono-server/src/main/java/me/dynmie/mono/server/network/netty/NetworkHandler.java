package me.dynmie.mono.server.network.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.dynmie.mono.server.client.ClientHandler;
import me.dynmie.mono.server.client.session.SessionHandler;
import me.dynmie.mono.server.data.ServerConfig;
import me.dynmie.mono.server.network.connection.ConnectionHandler;

import java.net.InetAddress;
import java.util.logging.Logger;

/**
 * @author dynmie
 */
public class NetworkHandler {

    private final Logger logger;
    private final ServerConfig.NetworkInformation networkInformation;
    private final ConnectionHandler connectionHandler;
    private final SessionHandler sessionHandler;
    private final ClientHandler clientHandler;

    private Channel serverChannel;


    public NetworkHandler(Logger logger, ServerConfig.NetworkInformation networkInformation, ConnectionHandler connectionHandler, SessionHandler sessionHandler, ClientHandler clientHandler) {
        this.logger = logger;
        this.networkInformation = networkInformation;
        this.connectionHandler = connectionHandler;
        this.sessionHandler = sessionHandler;
        this.clientHandler = clientHandler;
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
                        pipeline.addLast(new PacketDecoder(connectionHandler, sessionHandler, clientHandler, logger));
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
