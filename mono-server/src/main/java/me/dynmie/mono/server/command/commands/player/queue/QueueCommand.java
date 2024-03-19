package me.dynmie.mono.server.command.commands.player.queue;

import me.dynmie.jeorge.Inject;
import me.dynmie.jeorge.Injector;
import me.dynmie.mono.server.client.RemoteClient;
import me.dynmie.mono.server.command.handler.BaseCommand;
import me.dynmie.mono.server.command.handler.CommandContext;
import me.dynmie.mono.server.command.handler.CommandHandler;
import me.dynmie.mono.server.command.handler.CommandResult;
import me.dynmie.mono.shared.player.PlayerVideoInfo;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

/**
 * @author dynmie
 */
public class QueueCommand extends BaseCommand {
    private final CommandHandler commandHandler;

    @Inject
    public QueueCommand(Injector injector, CommandHandler commandHandler) {
        super(List.of("queue"));

        this.commandHandler = commandHandler;

        setUsage("<add|client|remove>");
        setMaxArgs(1);
        setMinArgs(1);
        setDescription("Edit a client's queue");

        Stream.of(
                QueueRemoveCommand.class,
                QueueAddCommand.class
        ).map(injector::createInstance).forEach(this::addSubcommand);

        addResolver(0, RemoteClient.class);
    }

    @Override
    public CommandResult onExecute(CommandContext context) {
        RemoteClient client = context.getAt(0, RemoteClient.class);

        commandHandler.execute("np " + client.getUniqueId());

        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("");
        joiner.add("Queue");
        for (int i = 0; i < client.getQueue().size(); i++) {
            PlayerVideoInfo info = client.getQueue().get(i);
            joiner.add("%s. %s (%s)".formatted(
                    i + 1,
                    info.getTitle(),
                    info.getVideoId()
            ));
        }

        context.sendMessage(joiner.toString());
        return CommandResult.OK;
    }
}
