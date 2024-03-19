package me.dynmie.mono.shared.packet.ready;

import me.dynmie.mono.shared.packet.PacketStore;
import me.dynmie.mono.shared.packet.ready.client.ClientboundPlayerPausePacket;
import me.dynmie.mono.shared.packet.ready.client.ClientboundPlayerPlayPacket;
import me.dynmie.mono.shared.packet.ready.client.ClientboundPlayerPlaylistUpdatePacket;
import me.dynmie.mono.shared.packet.ready.client.ClientboundPlayerSkipPacket;

/**
 * @author dynmie
 */
public class ClientboundReadyPacketStore extends PacketStore {
    @Override
    public void configure() {
        put(0, ClientboundPlayerPlayPacket.class);
        put(1, ClientboundPlayerPausePacket.class);
        put(2, ClientboundPlayerPlaylistUpdatePacket.class);
        put(3, ClientboundPlayerSkipPacket.class);
    }
}
