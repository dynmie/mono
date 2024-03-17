package me.dynmie.mono.shared.packet.handshake.client;

import me.dynmie.mono.shared.packet.Packet;
import me.dynmie.mono.shared.packet.handshake.ClientboundHandshakePacketHandler;

/**
 * @author dynmie
 */
public class ClientboundHandshakeResponsePacket extends Packet<ClientboundHandshakePacketHandler> {
    @Override
    public void handle(ClientboundHandshakePacketHandler handler) {
        handler.onHandshakeResponse(this);
    }
}
