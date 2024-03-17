package me.dynmie.mono.server.network.handler;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import me.dynmie.jeorge.Injector;
import me.dynmie.mono.server.network.connection.ClientConnection;
import me.dynmie.mono.shared.packet.ConnectionState;
import me.dynmie.mono.shared.packet.handshake.ServerboundHandshakePacketHandler;
import me.dynmie.mono.shared.packet.handshake.client.ClientboundHandshakeResponsePacket;
import me.dynmie.mono.shared.packet.handshake.server.ServerboundHandshakePacket;
import me.dynmie.mono.shared.packet.handshake.server.ServerboundTestPacket;

/**
 * @author dynmie
 */
@AllArgsConstructor
public class DefaultServerboundHandshakePacketHandler implements ServerboundHandshakePacketHandler {
    private final Injector injector;
    private final ClientConnection connection;

    @Override
    public void onTestPacket(ServerboundTestPacket packet) {
        connection.getLogger().info("Test packet received with message: " + packet.getString());
    }

    @Override
    public void onHandshake(ServerboundHandshakePacket packet) {
        Channel channel = connection.getChannel();

        connection.getLogger().info("Handshake connection established from " + channel.remoteAddress() + ".");

        connection.sendPacket(new ClientboundHandshakeResponsePacket());

        connection.setPacketHandler(new DefaultServerboundLoginPacketHandler(injector, connection));
        connection.setConnectionState(ConnectionState.LOGIN);
    }
}
