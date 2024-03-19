package me.dynmie.mono.shared.packet.handshake.server;

import me.dynmie.mono.shared.packet.Packet;
import me.dynmie.mono.shared.packet.handshake.ServerboundHandshakePacketHandler;

/**
 * @author dynmie
 */
public class ServerboundHandshakePacket implements Packet<ServerboundHandshakePacketHandler> {
    @Override
    public void handle(ServerboundHandshakePacketHandler handler) {
        handler.onHandshake(this);
    }
}
