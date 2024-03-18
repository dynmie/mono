package me.dynmie.mono.shared.packet.login;

import me.dynmie.mono.shared.packet.PacketStore;
import me.dynmie.mono.shared.packet.login.server.ServerboundLoginPacket;

/**
 * @author dynmie
 */
public class ServerboundLoginPacketStore extends PacketStore {
    @Override
    public void configure() {
        put(0, ServerboundLoginPacket.class);
    }
}
