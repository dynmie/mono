package me.dynmie.mono.shared.packet.store.login;

import me.dynmie.mono.shared.packet.login.client.ClientboundLoginFailedPacket;
import me.dynmie.mono.shared.packet.login.client.ClientboundLoginResponsePacket;
import me.dynmie.mono.shared.packet.store.PacketStore;

/**
 * @author dynmie
 */
public class ClientboundLoginPacketStore extends PacketStore {
    @Override
    public void configure() {
        put(0, ClientboundLoginResponsePacket.class);
        put(1, ClientboundLoginFailedPacket.class);
    }
}
