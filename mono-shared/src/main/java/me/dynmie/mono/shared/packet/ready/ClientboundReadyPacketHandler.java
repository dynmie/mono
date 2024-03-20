package me.dynmie.mono.shared.packet.ready;

import me.dynmie.mono.shared.packet.PacketHandler;
import me.dynmie.mono.shared.packet.ready.client.*;

/**
 * @author dynmie
 */
public interface ClientboundReadyPacketHandler extends PacketHandler {
    void onPlayerPlay(ClientboundPlayerPlayPacket packet);
    void onPlayerPause(ClientboundPlayerPausePacket packet);
    void onSkip(ClientboundPlayerSkipPacket packet);
    void onPlaylistUpdate(ClientboundPlayerPlaylistUpdatePacket packet);
    void onPlayerConfigUpdate(ClientboundPlayerConfigUpdatePacket packet);
}
