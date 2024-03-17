package me.dynmie.mono.shared.packet.login;

import me.dynmie.mono.shared.packet.PacketHandler;
import me.dynmie.mono.shared.packet.login.client.ClientboundLoginFailedPacket;
import me.dynmie.mono.shared.packet.login.client.ClientboundLoginResponsePacket;

/**
 * @author dynmie
 */
public interface ClientboundLoginPacketHandler extends PacketHandler {
    void onLoginResponse(ClientboundLoginResponsePacket packet);

    void onLoginFailed(ClientboundLoginFailedPacket packet);
}
