package me.dynmie.mono.shared.packet.ready.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.dynmie.mono.shared.packet.Packet;
import me.dynmie.mono.shared.packet.ready.ClientboundReadyPacketHandler;
import me.dynmie.mono.shared.player.PlayerPlaylistInfo;

/**
 * @author dynmie
 */
@Getter
@AllArgsConstructor
public class ClientboundPlayerPlaylistUpdatePacket implements Packet<ClientboundReadyPacketHandler> {
    private final PlayerPlaylistInfo playlistInfo;

    @Override
    public void handle(ClientboundReadyPacketHandler handler) {
        handler.onPlaylistUpdate(this);
    }
}
