package me.dynmie.mono.server.network.handler;

import me.dynmie.jeorge.Injector;
import me.dynmie.mono.server.network.connection.ClientConnection;
import me.dynmie.mono.shared.packet.ready.ServerboundReadyPacketHandler;

/**
 * @author dynmie
 */
public class DefaultServerboundReadyPacketHandler implements ServerboundReadyPacketHandler {
    private final Injector injector;
    private final ClientConnection connection;

    public DefaultServerboundReadyPacketHandler(Injector injector, ClientConnection connection) {
        this.injector = injector;
        this.connection = connection;
    }
}
