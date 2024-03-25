package me.dynmie.mono.shared.packet.ready;

import me.dynmie.mono.shared.packet.PacketHandler;
import me.dynmie.mono.shared.packet.ready.server.ServerboundPlayerInfoPacket;
import me.dynmie.mono.shared.packet.ready.server.ServerboundPlayerPlaylistUpdatePacket;

/**
 * @author dynmie
 */
public interface ServerboundReadyPacketHandler extends PacketHandler {
    void onPlaylistUpdate(ServerboundPlayerPlaylistUpdatePacket packet);
    void onPlayerInfo(ServerboundPlayerInfoPacket packet);
}
