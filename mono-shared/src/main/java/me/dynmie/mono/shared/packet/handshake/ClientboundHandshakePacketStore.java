package me.dynmie.mono.shared.packet.handshake;

import me.dynmie.mono.shared.packet.PacketStore;
import me.dynmie.mono.shared.packet.handshake.client.ClientboundHandshakeResponsePacket;

/**
 * @author dynmie
 */
public class ClientboundHandshakePacketStore extends PacketStore {
    @Override
    public void configure() {
        put(0, ClientboundHandshakeResponsePacket.class);
    }
}
