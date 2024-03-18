package me.dynmie.mono.shared.packet.login;

import me.dynmie.mono.shared.packet.PacketStore;
import me.dynmie.mono.shared.packet.login.client.ClientboundLoginFailedPacket;
import me.dynmie.mono.shared.packet.login.client.ClientboundLoginResponsePacket;

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
