package me.dynmie.mono.shared.packet.handshake;

import me.dynmie.mono.shared.packet.PacketStore;
import me.dynmie.mono.shared.packet.handshake.server.ServerboundHandshakePacket;
import me.dynmie.mono.shared.packet.handshake.server.ServerboundTestPacket;

/**
 * @author dynmie
 */
public class ServerboundHandshakePacketStore extends PacketStore {
    @Override
    public void configure() {
        put(0, ServerboundTestPacket.class);
        put(1, ServerboundHandshakePacket.class);
    }
}
