package me.dynmie.mono.server.command.commands.player.queue;

import me.dynmie.mono.server.client.RemoteClient;
import me.dynmie.mono.server.command.handler.BaseCommand;
import me.dynmie.mono.server.command.handler.CommandContext;
import me.dynmie.mono.server.command.handler.CommandResult;
import me.dynmie.mono.shared.player.PlayerVideoInfo;

import java.util.List;

/**
 * @author dynmie
 */
public class QueueRemoveCommand extends BaseCommand {
    public QueueRemoveCommand() {
        super(List.of("remove"));

        setDescription("Remove a video from a client's queue");
        setUsage("<client> <index>");

        setMinArgs(2);
        setMaxArgs(2);

        addResolver(0, RemoteClient.class);
    }

    @Override
    public CommandResult onExecute(CommandContext context) {
        RemoteClient client = context.getAt(0, RemoteClient.class);
        List<PlayerVideoInfo> queue = client.getQueue();

        int index;
        try {
            index = Integer.parseInt(context.getArgAt(1));
        } catch (NumberFormatException e) {
            return CommandResult.INCORRECT_USAGE;
        }
        index = index - 1;

        if (index < 0 || index > queue.size()) {
            context.sendMessage("That video doesn't exist.");
            return CommandResult.OK;
        }

        PlayerVideoInfo removed = client.getQueue().remove(index);
        client.sendPlaylistInfo();
        context.sendMessage("The video '" + removed.getTitle() + "' (" + removed.getVideoId() + ") has been removed from the queue.");

        return CommandResult.OK;
    }
}
