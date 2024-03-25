package me.dynmie.mono.shared.packet.login.client;

import me.dynmie.mono.shared.packet.Packet;
import me.dynmie.mono.shared.packet.login.ClientboundLoginPacketHandler;

/**
 * @author dynmie
 */
public class ClientboundLoginFailedPacket implements Packet<ClientboundLoginPacketHandler> {
    @Override
    public void handle(ClientboundLoginPacketHandler handler) {
        handler.onLoginFailed(this);
    }
}
