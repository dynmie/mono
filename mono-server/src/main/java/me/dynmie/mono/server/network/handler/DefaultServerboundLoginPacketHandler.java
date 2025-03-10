package me.dynmie.mono.server.network.handler;

import me.dynmie.jeorge.Injector;
import me.dynmie.mono.server.client.ClientService;
import me.dynmie.mono.server.client.RemoteClient;
import me.dynmie.mono.server.client.session.SessionService;
import me.dynmie.mono.server.data.ServerConfig;
import me.dynmie.mono.server.network.connection.ClientConnection;
import me.dynmie.mono.server.player.VideoService;
import me.dynmie.mono.shared.packet.ConnectionState;
import me.dynmie.mono.shared.packet.login.ServerboundLoginPacketHandler;
import me.dynmie.mono.shared.packet.login.client.ClientboundLoginFailedPacket;
import me.dynmie.mono.shared.packet.login.client.ClientboundLoginResponsePacket;
import me.dynmie.mono.shared.packet.login.server.ServerboundLoginPacket;
import me.dynmie.mono.shared.packet.ready.client.ClientboundPlayerPlaylistUpdatePacket;
import me.dynmie.mono.shared.player.PlayerPlaylistInfo;
import me.dynmie.mono.shared.session.ClientSession;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author dynmie
 */
public class DefaultServerboundLoginPacketHandler implements ServerboundLoginPacketHandler {
    private final Injector injector;
    private final ClientConnection connection;

    private final Logger logger;
    private final ServerConfig.LoginCredentials loginCredentials;

    public DefaultServerboundLoginPacketHandler(Injector injector, ClientConnection connection) {
        this.injector = injector;
        this.connection = connection;

        logger = injector.getDependency(Logger.class);
        loginCredentials = injector.getDependency(ServerConfig.class).getLoginCredentials();
    }

    @Override
    public void onLoginAttempt(ServerboundLoginPacket packet) {
        String correctToken = loginCredentials.getToken();
        String token = packet.getToken();

        if (!correctToken.equals(token)) {
            connection.sendPacket(new ClientboundLoginFailedPacket());
            connection.closeConnection();

            logger.info("Client from " + connection.getChannel().remoteAddress() + " had incorrect credentials and has failed to log in.");
            return;
        }

        SessionService sessionService = injector.getDependency(SessionService.class);
        ClientService clientService = injector.getDependency(ClientService.class);
        VideoService videoService = injector.getDependency(VideoService.class);

        String clientName = "default";
        UUID uniqueId = UUID.randomUUID();

        if (clientService.getClient(clientName) != null) {
            String string = uniqueId.toString();
            clientName = string.substring(string.length() - 4);

            // name collisions lmao
            if (clientService.getClient(clientName) != null) {
                clientName = string;
            }
        }

        ClientSession session = new ClientSession(clientName, uniqueId);
        sessionService.addSession(connection.getChannel(), session);

        RemoteClient found = clientService.getClient(uniqueId);
        if (found != null) {
            connection.sendPacket(new ClientboundLoginFailedPacket());
            connection.closeConnection();

            logger.severe("Client from " + connection.getChannel().remoteAddress() + " had conflicting UUIDs with an existing client! Client has been denied login.");
            return;
        }

        RemoteClient client = new RemoteClient(
                session,
                connection,
                new ArrayList<>(videoService.generateDefaultPlaylistInfo().getVideos())
        );
        clientService.addClient(client);

        // wierd fix, apparently netty doesn't run future listeners for some reason
        // can't seem to find a solution so i did this instead
        Thread.startVirtualThread(() -> {
            connection.sendPacket(new ClientboundLoginResponsePacket(session))
                    .awaitUninterruptibly(2, TimeUnit.MILLISECONDS);

            connection.setPacketHandler(new DefaultServerboundReadyPacketHandler(injector, client, connection));
            connection.setConnectionState(ConnectionState.READY);

            connection.sendPacket(new ClientboundPlayerPlaylistUpdatePacket(
                    new PlayerPlaylistInfo(client.getQueue())
            ));

            logger.info("Client %s[%s](%s) has successfully logged in!".formatted(
                    session.getName(),
                    session.getUniqueId(),
                    connection.getChannel().remoteAddress()
            ));
        });
    }
}
