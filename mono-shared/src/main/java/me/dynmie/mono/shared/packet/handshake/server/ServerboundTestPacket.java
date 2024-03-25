package me.dynmie.mono.shared.packet.handshake.server;

import lombok.Getter;
import me.dynmie.mono.shared.packet.Packet;
import me.dynmie.mono.shared.packet.handshake.ServerboundHandshakePacketHandler;

/**
 * @author dynmie
 */
@Getter
public class ServerboundTestPacket implements Packet<ServerboundHandshakePacketHandler> {
    private final String string;

    public ServerboundTestPacket(String string) {
        this.string = string;
    }

    @Override
    public void handle(ServerboundHandshakePacketHandler handler) {
        handler.onTestPacket(this);
    }
}
