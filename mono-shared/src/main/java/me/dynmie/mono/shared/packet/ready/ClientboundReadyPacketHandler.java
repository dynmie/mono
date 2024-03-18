package me.dynmie.mono.shared.packet.ready;

import me.dynmie.mono.shared.packet.PacketHandler;
import me.dynmie.mono.shared.packet.ready.client.ClientboundPlayerPausePacket;
import me.dynmie.mono.shared.packet.ready.client.ClientboundPlayerPlayPacket;

/**
 * @author dynmie
 */
public interface ClientboundReadyPacketHandler extends PacketHandler {
    void onPlayerPlay(ClientboundPlayerPlayPacket packet);
    void onPlayerPause(ClientboundPlayerPausePacket packet);
}
