package me.dynmie.mono.shared.packet.ready.client;

import me.dynmie.mono.shared.packet.Packet;
import me.dynmie.mono.shared.packet.ready.ClientboundReadyPacketHandler;

/**
 * @author dynmie
 */
public class ClientboundPlayerPlayPacket implements Packet<ClientboundReadyPacketHandler> {
    @Override
    public void handle(ClientboundReadyPacketHandler handler) {
        handler.onPlayerPlay(this);
    }
}
