package me.dynmie.mono.shared.packet.store.handshake;

import me.dynmie.mono.shared.packet.handshake.client.ClientboundHandshakeResponsePacket;
import me.dynmie.mono.shared.packet.store.PacketStore;

/**
 * @author dynmie
 */
public class ClientboundHandshakePacketStore extends PacketStore {
    @Override
    public void configure() {
        put(0, ClientboundHandshakeResponsePacket.class);
    }
}
