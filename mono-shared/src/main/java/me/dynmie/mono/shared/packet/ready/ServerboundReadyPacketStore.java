package me.dynmie.mono.shared.packet.ready;

import me.dynmie.mono.shared.packet.PacketStore;
import me.dynmie.mono.shared.packet.ready.server.ServerboundPlayerInfoPacket;
import me.dynmie.mono.shared.packet.ready.server.ServerboundPlayerPlaylistUpdatePacket;

/**
 * @author dynmie
 */
public class ServerboundReadyPacketStore extends PacketStore {
    @Override
    public void configure() {
        put(0, ServerboundPlayerPlaylistUpdatePacket.class);
        put(1, ServerboundPlayerInfoPacket.class);
    }
}
