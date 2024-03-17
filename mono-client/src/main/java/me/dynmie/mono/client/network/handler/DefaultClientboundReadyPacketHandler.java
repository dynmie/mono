package me.dynmie.mono.client.network.handler;

import me.dynmie.jeorge.Injector;
import me.dynmie.mono.client.network.connection.ServerConnection;
import me.dynmie.mono.shared.packet.ready.ClientboundReadyPacketHandler;

/**
 * @author dynmie
 */
public class DefaultClientboundReadyPacketHandler implements ClientboundReadyPacketHandler {

    private final Injector injector;
    private final ServerConnection serverConnection;

    public DefaultClientboundReadyPacketHandler(Injector injector, ServerConnection serverConnection) {
        this.injector = injector;
        this.serverConnection = serverConnection;
    }
}
