package me.dynmie.mono.server.command.commands;

import me.dynmie.mono.server.command.handler.BaseCommand;
import me.dynmie.mono.server.command.handler.CommandContext;
import me.dynmie.mono.server.command.handler.CommandResult;

import java.util.List;
import java.util.StringJoiner;

/**
 * @author dynmie
 */
public class ServerHelpCommand extends BaseCommand {
    public ServerHelpCommand() {
        super(List.of("help", "?"));

        setDescription("Show a list of all commands.");
    }

    @Override
    public CommandResult onExecute(CommandContext context) {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("Commands");
        for (BaseCommand command : context.getHandler().getCommands().values()) {
            joiner.add("/%s%s - %s".formatted(
                    command.getName(),
                    command.getUsage().isEmpty() ? "" : " " + command.getUsage(),
                    command.getDescription()
            ));
        }
        context.sendMessage(joiner.toString());
        return CommandResult.OK;
    }
}
