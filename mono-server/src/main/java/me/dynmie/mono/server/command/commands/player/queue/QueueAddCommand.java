package me.dynmie.mono.server.command.commands.player.queue;

import me.dynmie.jeorge.Inject;
import me.dynmie.mono.server.client.RemoteClient;
import me.dynmie.mono.server.command.handler.BaseCommand;
import me.dynmie.mono.server.command.handler.CommandContext;
import me.dynmie.mono.server.command.handler.CommandResult;
import me.dynmie.mono.server.player.VideoService;
import me.dynmie.mono.shared.player.PlayerVideoInfo;

import java.util.List;

/**
 * @author dynmie
 */
public class QueueAddCommand extends BaseCommand {
    @Inject
    private VideoService videoService;

    public QueueAddCommand() {
        super(List.of("add"));

        setDescription("Add a video to a client's queue");
        setUsage("<client> <id>");

        setMinArgs(2);
        setMaxArgs(3);

        addResolver(0, RemoteClient.class);
        addResolver(2, Boolean.class);
    }

    @Override
    public CommandResult onExecute(CommandContext context) {
        RemoteClient client = context.getAt(0, RemoteClient.class);

        boolean def = false;
        if (context.size() > 2) {
            def = context.getAt(2, Boolean.class);
        }

        PlayerVideoInfo videoInfo = videoService.getVideoInfo(context.getArgAt(1), def);
        if (videoInfo == null) {
            context.sendMessage("That video doesn't exist. Are you using the id instead of the link?");
            return CommandResult.OK;
        }

        client.addToQueue(videoInfo);
        client.sendPlaylistInfo();
        context.sendMessage("Video has been sent to the client.");

        return CommandResult.OK;
    }
}
