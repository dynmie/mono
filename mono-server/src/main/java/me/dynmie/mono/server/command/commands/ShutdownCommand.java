package me.dynmie.mono.server.command.commands;

import me.dynmie.jeorge.Inject;
import me.dynmie.mono.server.Server;
import me.dynmie.mono.server.command.handler.BaseCommand;
import me.dynmie.mono.server.command.handler.CommandContext;
import me.dynmie.mono.server.command.handler.CommandResult;

import java.util.List;

/**
 * @author dynmie
 */
public class ShutdownCommand extends BaseCommand {
    private final Server server;

    @Inject
    public ShutdownCommand(Server server) {
        super(List.of("shutdown"));
        this.server = server;

        setMinArgs(0);
        setMaxArgs(0);
        setDescription("Shutdown the server");
    }

    @Override
    public CommandResult onExecute(CommandContext context) {
        server.shutdown();
        return CommandResult.OK;
    }
}
