package me.dynmie.mono.server.network.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.AttributeKey;
import lombok.Getter;
import lombok.Setter;
import me.dynmie.jeorge.Injector;
import me.dynmie.mono.server.network.handler.DefaultServerboundHandshakePacketHandler;
import me.dynmie.mono.shared.packet.*;

import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * @author dynmie
 */
public class ClientConnection {
    public static final AttributeKey<ConnectionState> PROTOCOL_ATTR = AttributeKey.valueOf("protocol");

    private final Injector injector;
    private final @Getter Logger logger;
    private final @Getter Channel channel;
    private ConnectionState connectionState;

    private @Getter @Setter PacketHandler packetHandler;

    private final ReentrantLock channelLock = new ReentrantLock();

    public ClientConnection(Injector injector, Channel channel) {
        this.injector = injector;
        this.logger = injector.getDependency(Logger.class);
        this.channel = channel;

        this.packetHandler = new DefaultServerboundHandshakePacketHandler(injector, this);

        setConnectionState(ConnectionState.HANDSHAKE);
    }

    public void setConnectionState(ConnectionState connectionState) {
        channel.attr(PROTOCOL_ATTR).set(connectionState);
        this.connectionState = connectionState;
    }

    public ConnectionState getConnectionState() {
        ConnectionState cs = channel.attr(PROTOCOL_ATTR).get();
        if (cs != connectionState) {
            closeConnection();
            throw new IllegalStateException("Client protocol and server protocol not synced!");
        }
        return connectionState;
    }

    public boolean isConnected() {
        return channel.isActive();
    }

    public void closeConnection() {
        if (channel.isOpen()) {
            channel.close().awaitUninterruptibly();
        }
    }

    public ChannelFuture sendPacket(Packet<? extends PacketHandler> packet) {
        try {
            channelLock.lock();
            return channel.writeAndFlush(Packets.toRaw(getConnectionState(), PacketDirection.CLIENTBOUND, packet));
        } finally {
            if (channelLock.isLocked()) {
                channelLock.unlock();
            }
        }
    }
}
