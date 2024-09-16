package me.dynmie.mono.server.command.server;

import me.dynmie.mono.server.client.ClientService;
import me.dynmie.mono.server.client.RemoteClient;
import me.dynmie.mono.server.command.handler.CommandHandlerConfiguration;
import me.dynmie.mono.server.command.server.resolvers.ClientResolver;

/**
 * @author dynmie
 */
public class ServerCommandHandlerConfiguration extends CommandHandlerConfiguration {
    public ServerCommandHandlerConfiguration(ClientService clientService) {
        addResolver(RemoteClient.class, new ClientResolver(clientService));
    }
}
