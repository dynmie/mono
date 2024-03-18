package me.dynmie.mono.server.command.commands.clients;

import me.dynmie.mono.server.client.RemoteClient;
import me.dynmie.mono.server.command.handler.BaseCommand;
import me.dynmie.mono.server.command.handler.CommandContext;
import me.dynmie.mono.server.command.handler.CommandResult;

import java.util.List;
import java.util.StringJoiner;

/**
 * @author dynmie
 */
public class ClientInfoCommand extends BaseCommand {

    public ClientInfoCommand() {
        super(List.of("clientinfo"));

        setDescription("Get information about a client");
        setUsage("<client>");

        setMinArgs(1);
        setMaxArgs(1);

        addResolver(0, RemoteClient.class);
    }

    @Override
    public CommandResult onExecute(CommandContext context) {
        RemoteClient client = context.getAt(0, RemoteClient.class);

        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("Name: " + client.getName());
        joiner.add("Session ID: " + client.getUniqueId());
        joiner.add("Address: " + client.getAddress());

        context.sendMessage(joiner.toString());

        return null;
    }
}
