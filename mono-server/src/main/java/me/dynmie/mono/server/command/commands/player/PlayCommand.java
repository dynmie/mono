package me.dynmie.mono.server.command.commands.player;

import me.dynmie.mono.server.client.RemoteClient;
import me.dynmie.mono.server.command.handler.BaseCommand;
import me.dynmie.mono.server.command.handler.CommandContext;
import me.dynmie.mono.server.command.handler.CommandResult;

import java.util.List;

/**
 * @author dynmie
 */
public class PlayCommand extends BaseCommand {
    public PlayCommand() {
        super(List.of("play"));

        setDescription("Get the player to play");
        setUsage("<client>");

        setMinArgs(1);
        setMaxArgs(1);

        addResolver(0, RemoteClient.class);
    }

    @Override
    public CommandResult onExecute(CommandContext context) {
        RemoteClient client = context.getAt(0, RemoteClient.class);
        client.play();
        return CommandResult.OK;
    }
}
