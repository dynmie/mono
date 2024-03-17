package me.dynmie.mono.client.network.connection;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.Getter;
import lombok.Setter;
import me.dynmie.jeorge.Injector;
import me.dynmie.mono.client.network.handler.DefaultClientboundHandshakePacketHandler;
import me.dynmie.mono.shared.packet.*;

import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * @author dynmie
 */
public class ServerConnection {
    public static final AttributeKey<ConnectionState> PROTOCOL_ATTR = AttributeKey.valueOf("protocol");

    private final @Getter Injector injector;
    private final @Getter Logger logger;
    private final @Getter Channel channel;

    private @Getter @Setter PacketHandler packetHandler;

    private final ReentrantLock channelLock = new ReentrantLock();

    public ServerConnection(Injector injector, Logger logger, Channel channel) {
        this.injector = injector;
        this.logger = logger;
        this.channel = channel;

        this.packetHandler = new DefaultClientboundHandshakePacketHandler(injector, this);

        setConnectionState(ConnectionState.HANDSHAKE);
    }

    public ConnectionState getConnectionState() {
        return channel.attr(PROTOCOL_ATTR).get();
    }

    public void setConnectionState(ConnectionState connectionState) {
        channel.attr(PROTOCOL_ATTR).set(connectionState);
    }

    public void sendPacket(Packet<? extends PacketHandler> packet) {
        try {
            channelLock.lock();
            channel.writeAndFlush(Packets.toRaw(getConnectionState(), PacketDirection.SERVERBOUND, packet));
        } finally {
            if (channelLock.isLocked()) {
                channelLock.unlock();
            }
        }
    }

    public boolean isConnected() {
        return channel.isActive();
    }

    public void closeChannel() {
        if (channel.isOpen()) {
            channel.close().awaitUninterruptibly();
        }
    }
}
