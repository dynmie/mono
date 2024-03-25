package me.dynmie.mono.server.command.commands.clients;

import me.dynmie.jeorge.Inject;
import me.dynmie.mono.server.client.ClientHandler;
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
    private ClientHandler clientHandler;

    public ClientsCommand() {
        super(List.of("clients"));

        setMinArgs(0);
        setMaxArgs(0);

        setDescription("Get a list of all connected clients");
    }

    @Override
    public CommandResult onExecute(CommandContext context) {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("Connected clients (" + clientHandler.getConnectedClients().size() + ")");
        if (clientHandler.getConnectedClients().isEmpty()) {
            joiner.add("There are no clients connected.");
        }
        for (RemoteClient client : clientHandler.getConnectedClients()) {
            joiner.add(client.getName() + " - " + " Session ID: " + client.getUniqueId());
        }
        context.sendMessage(joiner.toString());
        return CommandResult.OK;
    }
}
