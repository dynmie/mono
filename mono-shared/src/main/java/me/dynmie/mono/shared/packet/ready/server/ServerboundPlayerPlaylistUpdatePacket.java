package me.dynmie.mono.shared.packet.ready.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.dynmie.mono.shared.packet.Packet;
import me.dynmie.mono.shared.packet.ready.ServerboundReadyPacketHandler;
import me.dynmie.mono.shared.player.PlayerPlaylistInfo;

/**
 * @author dynmie
 */
@Getter
@AllArgsConstructor
public class ServerboundPlayerPlaylistUpdatePacket extends Packet<ServerboundReadyPacketHandler> {
    private final PlayerPlaylistInfo playlistInfo;

    @Override
    public void handle(ServerboundReadyPacketHandler handler) {
        handler.onPlaylistUpdate(this);
    }
}
