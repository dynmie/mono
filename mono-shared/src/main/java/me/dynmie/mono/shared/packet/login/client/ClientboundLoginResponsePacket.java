package me.dynmie.mono.shared.packet.login.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.dynmie.mono.shared.packet.Packet;
import me.dynmie.mono.shared.packet.login.ClientboundLoginPacketHandler;
import me.dynmie.mono.shared.session.ClientSession;

/**
 * @author dynmie
 */
@Getter
@AllArgsConstructor
public class ClientboundLoginResponsePacket implements Packet<ClientboundLoginPacketHandler> {
    private final ClientSession session;

    @Override
    public void handle(ClientboundLoginPacketHandler handler) {
        handler.onLoginResponse(this);
    }
}
