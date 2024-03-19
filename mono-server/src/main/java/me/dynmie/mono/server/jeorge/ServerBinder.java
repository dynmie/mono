package me.dynmie.mono.server.jeorge;

import lombok.AllArgsConstructor;
import me.dynmie.jeorge.Binder;
import me.dynmie.mono.server.Server;
import me.dynmie.mono.server.client.ClientHandler;
import me.dynmie.mono.server.client.session.SessionHandler;
import me.dynmie.mono.server.command.handler.CommandHandler;
import me.dynmie.mono.server.data.ServerConfig;
import me.dynmie.mono.server.network.connection.ConnectionHandler;
import me.dynmie.mono.server.network.netty.NetworkHandler;
import me.dynmie.mono.server.player.VideoHandler;

import java.util.logging.Logger;

/**
 * @author dynmie
 */
@AllArgsConstructor
public class ServerBinder extends Binder {

    private final Server server;
    private final Logger logger;
    private final SessionHandler sessionHandler;
    private final ConnectionHandler connectionHandler;
    private final ClientHandler clientHandler;
    private final NetworkHandler networkHandler;
    private final VideoHandler videoHandler;
    private final CommandHandler commandHandler;
    private final ServerConfig config;

    @Override
    public void configure() {
        bind(Server.class, server);
        bind(Logger.class, logger);
        bind(SessionHandler.class, sessionHandler);
        bind(ConnectionHandler.class, connectionHandler);
        bind(ClientHandler.class, clientHandler);
        bind(NetworkHandler.class, networkHandler);
        bind(VideoHandler.class, videoHandler);
        bind(CommandHandler.class, commandHandler);
        bind(ServerConfig.class, config);
    }

}
