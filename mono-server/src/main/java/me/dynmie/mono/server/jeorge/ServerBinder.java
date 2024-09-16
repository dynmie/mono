package me.dynmie.mono.server.jeorge;

import lombok.AllArgsConstructor;
import me.dynmie.jeorge.Binder;
import me.dynmie.mono.server.Server;
import me.dynmie.mono.server.client.ClientService;
import me.dynmie.mono.server.client.session.SessionService;
import me.dynmie.mono.server.command.handler.CommandHandler;
import me.dynmie.mono.server.data.ServerConfig;
import me.dynmie.mono.server.network.connection.ConnectionService;
import me.dynmie.mono.server.network.netty.NetworkHandler;
import me.dynmie.mono.server.player.VideoService;

import java.util.logging.Logger;

/**
 * @author dynmie
 */
@AllArgsConstructor
public class ServerBinder extends Binder {

    private final Server server;
    private final Logger logger;
    private final SessionService sessionService;
    private final ConnectionService connectionService;
    private final ClientService clientService;
    private final NetworkHandler networkHandler;
    private final VideoService videoService;
    private final CommandHandler commandHandler;
    private final ServerConfig config;

    @Override
    public void configure() {
        bind(Server.class, server);
        bind(Logger.class, logger);
        bind(SessionService.class, sessionService);
        bind(ConnectionService.class, connectionService);
        bind(ClientService.class, clientService);
        bind(NetworkHandler.class, networkHandler);
        bind(VideoService.class, videoService);
        bind(CommandHandler.class, commandHandler);
        bind(ServerConfig.class, config);
    }

}
