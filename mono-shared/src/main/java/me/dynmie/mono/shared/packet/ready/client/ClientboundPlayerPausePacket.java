package me.dynmie.mono.shared.packet.ready.client;

import me.dynmie.mono.shared.packet.Packet;
import me.dynmie.mono.shared.packet.ready.ClientboundReadyPacketHandler;

/**
 * @author dynmie
 */
public class ClientboundPlayerPausePacket implements Packet<ClientboundReadyPacketHandler> {
    @Override
    public void handle(ClientboundReadyPacketHandler handler) {
        handler.onPlayerPause(this);
    }
}
