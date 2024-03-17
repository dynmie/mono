package me.dynmie.mono.shared.packet.login.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.dynmie.mono.shared.packet.Packet;
import me.dynmie.mono.shared.packet.login.ServerboundLoginPacketHandler;

/**
 * @author dynmie
 */
@Getter
@AllArgsConstructor
public class ServerboundLoginPacket extends Packet<ServerboundLoginPacketHandler> {
    private final String token;

    @Override
    public void handle(ServerboundLoginPacketHandler handler) {
        handler.onLoginAttempt(this);
    }
}
