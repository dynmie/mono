package me.dynmie.mono.shared.packet.handshake;

import me.dynmie.mono.shared.packet.PacketHandler;
import me.dynmie.mono.shared.packet.handshake.client.ClientboundHandshakeResponsePacket;

/**
 * @author dynmie
 */
public interface ClientboundHandshakePacketHandler extends PacketHandler {
    void onHandshakeResponse(ClientboundHandshakeResponsePacket packet);
}
