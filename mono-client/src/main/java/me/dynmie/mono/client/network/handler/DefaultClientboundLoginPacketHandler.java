package me.dynmie.mono.client.network.handler;

import lombok.AllArgsConstructor;
import me.dynmie.jeorge.Injector;
import me.dynmie.mono.client.network.NetworkHandler;
import me.dynmie.mono.client.network.connection.ServerConnection;
import me.dynmie.mono.shared.packet.ConnectionState;
import me.dynmie.mono.shared.packet.login.ClientboundLoginPacketHandler;
import me.dynmie.mono.shared.packet.login.client.ClientboundLoginFailedPacket;
import me.dynmie.mono.shared.packet.login.client.ClientboundLoginResponsePacket;
import me.dynmie.mono.shared.session.ClientSession;

import java.util.UUID;

/**
 * @author dynmie
 */
@AllArgsConstructor
public class DefaultClientboundLoginPacketHandler implements ClientboundLoginPacketHandler {
    private final Injector injector;
    private final ServerConnection connection;

    @Override
    public void onLoginResponse(ClientboundLoginResponsePacket packet) {
        NetworkHandler networkHandler = injector.getDependency(NetworkHandler.class);

        ClientSession session = packet.getSession();
        networkHandler.setSession(session);

        String clientName = session.getName();
        UUID uniqueId = session.getUniqueId();

        connection.setPacketHandler(new DefaultClientboundReadyPacketHandler(injector, connection));
        connection.setConnectionState(ConnectionState.READY);

        connection.getLogger().info("Successfully logged in as '" + clientName + "' with id '" + uniqueId + "'!");
    }

    @Override
    public void onLoginFailed(ClientboundLoginFailedPacket packet) {
        connection.getLogger().severe("Failed to log in! Does the server token and the client token match?");
    }
}
