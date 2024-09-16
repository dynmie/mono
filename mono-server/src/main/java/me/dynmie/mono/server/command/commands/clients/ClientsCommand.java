package me.dynmie.mono.server.command.commands.clients;

import me.dynmie.jeorge.Inject;
import me.dynmie.mono.server.client.ClientService;
import me.dynmie.mono.server.client.RemoteClient;
import me.dynmie.mono.server.command.handler.BaseCommand;
import me.dynmie.mono.server.command.handler.CommandContext;
import me.dynmie.mono.server.command.handler.CommandResult;

import java.util.List;
import java.util.StringJoiner;

/**
 * @author dynmie
 */
public class ClientsCommand extends BaseCommand {
    @Inject
    private ClientService clientService;

    public ClientsCommand() {
        super(List.of("clients"));

        setMinArgs(0);
        setMaxArgs(0);

        setDescription("Get a list of all connected clients");
    }

    @Override
    public CommandResult onExecute(CommandContext context) {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("Connected clients (" + clientService.getConnectedClients().size() + ")");
        if (clientService.getConnectedClients().isEmpty()) {
            joiner.add("There are no clients connected.");
        }
        for (RemoteClient client : clientService.getConnectedClients()) {
            joiner.add(client.getName() + " - " + " Session ID: " + client.getUniqueId());
        }
        context.sendMessage(joiner.toString());
        return CommandResult.OK;
    }
}
