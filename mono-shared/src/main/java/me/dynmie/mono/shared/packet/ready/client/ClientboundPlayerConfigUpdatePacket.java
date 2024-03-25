package me.dynmie.mono.shared.packet.ready.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.dynmie.mono.shared.packet.Packet;
import me.dynmie.mono.shared.packet.ready.ClientboundReadyPacketHandler;
import me.dynmie.mono.shared.player.PlayerConfig;

/**
 * @author dynmie
 */
@Getter
@AllArgsConstructor
public class ClientboundPlayerConfigUpdatePacket implements Packet<ClientboundReadyPacketHandler> {
    private final PlayerConfig config;

    @Override
    public void handle(ClientboundReadyPacketHandler handler) {
        handler.onPlayerConfigUpdate(this);
    }
}
