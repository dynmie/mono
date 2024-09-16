package me.dynmie.mono.server.command.server.resolvers;

import lombok.AllArgsConstructor;
import me.dynmie.mono.server.client.ClientService;
import me.dynmie.mono.server.client.RemoteClient;
import me.dynmie.mono.server.command.handler.CommandContext;
import me.dynmie.mono.server.command.handler.CommandResult;
import me.dynmie.mono.server.command.handler.resolver.ArgumentResolver;
import me.dynmie.mono.server.command.handler.resolver.UnresolvedException;

import java.util.UUID;

/**
 * @author dynmie
 */
@AllArgsConstructor
public class ClientResolver implements ArgumentResolver<RemoteClient> {
    private final ClientService clientService;

    @Override
    public RemoteClient resolve(CommandContext context, String arg) {
        try {
            UUID uuid = UUID.fromString(arg);
            RemoteClient client = clientService.getClient(uuid);
            if (client != null) {
                return client;
            }
        } catch (IllegalArgumentException ignored) {

        }

        RemoteClient client = clientService.getClient(arg);
        if (client == null) {
            throw new UnresolvedException();
        }

        return client;
    }

    @Override
    public CommandResult failed(CommandContext context) {
        context.sendMessage("That client doesn't exist!");
        return CommandResult.SILENT_FAIL;
    }
}
