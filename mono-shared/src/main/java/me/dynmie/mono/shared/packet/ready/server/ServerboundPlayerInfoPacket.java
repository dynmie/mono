package me.dynmie.mono.shared.packet.ready.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.dynmie.mono.shared.packet.Packet;
import me.dynmie.mono.shared.packet.ready.ServerboundReadyPacketHandler;
import me.dynmie.mono.shared.player.PlayerInfo;

/**
 * @author dynmie
 */
@Getter
@AllArgsConstructor
public class ServerboundPlayerInfoPacket extends Packet<ServerboundReadyPacketHandler> {
    private final PlayerInfo playerInfo;

    @Override
    public void handle(ServerboundReadyPacketHandler handler) {
        handler.onPlayerInfo(this);
    }
}
