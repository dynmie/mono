package me.dynmie.mono.shared.packet.store.login;

import me.dynmie.mono.shared.packet.login.server.ServerboundLoginPacket;
import me.dynmie.mono.shared.packet.store.PacketStore;

/**
 * @author dynmie
 */
public class ServerboundLoginPacketStore extends PacketStore {
    @Override
    public void configure() {
        put(0, ServerboundLoginPacket.class);
    }
}
