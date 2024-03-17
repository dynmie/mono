package me.dynmie.mono.shared.packet.handshake;

import me.dynmie.mono.shared.packet.PacketHandler;
import me.dynmie.mono.shared.packet.handshake.server.ServerboundHandshakePacket;
import me.dynmie.mono.shared.packet.handshake.server.ServerboundTestPacket;

/**
 * @author dynmie
 */
public interface ServerboundHandshakePacketHandler extends PacketHandler {

    void onTestPacket(ServerboundTestPacket packet);

    void onHandshake(ServerboundHandshakePacket packet);

}
