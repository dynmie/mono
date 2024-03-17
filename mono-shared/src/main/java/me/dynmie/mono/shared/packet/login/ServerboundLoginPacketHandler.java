package me.dynmie.mono.shared.packet.login;

import me.dynmie.mono.shared.packet.PacketHandler;
import me.dynmie.mono.shared.packet.login.server.ServerboundLoginPacket;

/**
 * @author dynmie
 */
public interface ServerboundLoginPacketHandler extends PacketHandler {
    void onLoginAttempt(ServerboundLoginPacket packet);
}
