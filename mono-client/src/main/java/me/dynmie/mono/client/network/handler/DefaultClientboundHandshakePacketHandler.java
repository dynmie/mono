package me.dynmie.mono.client.network.handler;

import me.dynmie.jeorge.Injector;
import me.dynmie.mono.client.data.ClientConfig;
import me.dynmie.mono.client.network.connection.ServerConnection;
import me.dynmie.mono.shared.packet.ConnectionState;
import me.dynmie.mono.shared.packet.handshake.ClientboundHandshakePacketHandler;
import me.dynmie.mono.shared.packet.handshake.client.ClientboundHandshakeResponsePacket;
import me.dynmie.mono.shared.packet.login.server.ServerboundLoginPacket;

/**
 * @author dynmie
 */
public class DefaultClientboundHandshakePacketHandler implements ClientboundHandshakePacketHandler {
    private final Injector injector;
    private final ServerConnection connection;

    private final ClientConfig.LoginCredentials loginCredentials;

    public DefaultClientboundHandshakePacketHandler(Injector injector, ServerConnection connection) {
        this.injector = injector;
        this.connection = connection;

        this.loginCredentials = injector.getDependency(ClientConfig.class).getLoginCredentials();
    }

    @Override
    public void onHandshakeResponse(ClientboundHandshakeResponsePacket packet) {
        connection.setPacketHandler(new DefaultClientboundLoginPacketHandler(injector, connection));
        connection.setConnectionState(ConnectionState.LOGIN);

        connection.getLogger().info("Attempting login with credentials...");
        connection.sendPacket(new ServerboundLoginPacket(
                loginCredentials.getToken()
        ));
    }
}
