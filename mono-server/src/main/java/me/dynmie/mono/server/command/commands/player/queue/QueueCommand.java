package me.dynmie.mono.server.command.commands.player.queue;

import me.dynmie.jeorge.Inject;
import me.dynmie.jeorge.Injector;
import me.dynmie.mono.server.client.RemoteClient;
import me.dynmie.mono.server.command.handler.BaseCommand;
import me.dynmie.mono.server.command.handler.CommandContext;
import me.dynmie.mono.server.command.handler.CommandResult;
import me.dynmie.mono.shared.player.PlayerVideoInfo;

import java.util.List;
import java.util.StringJoiner;

/**
 * @author dynmie
 */
public class QueueCommand extends BaseCommand {
    @Inject
    public QueueCommand(Injector injector) {
        super(List.of("queue"));

        setUsage("<add|client>");
        setMaxArgs(1);
        setMinArgs(1);
        setDescription("Edit a client's queue");

        addSubcommand(injector.createInstance(QueueAddCommand.class));
        addResolver(0, RemoteClient.class);
    }

    @Override
    public CommandResult onExecute(CommandContext context) {
        RemoteClient client = context.getAt(0, RemoteClient.class);

        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("Queue (" + client.getName() + ")");
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
