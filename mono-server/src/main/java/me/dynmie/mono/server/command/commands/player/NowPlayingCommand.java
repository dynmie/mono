package me.dynmie.mono.server.command.commands.player;

import me.dynmie.mono.server.client.RemoteClient;
import me.dynmie.mono.server.command.handler.BaseCommand;
import me.dynmie.mono.server.command.handler.CommandContext;
import me.dynmie.mono.server.command.handler.CommandResult;
import me.dynmie.mono.shared.player.PlayerInfo;

import java.util.List;
import java.util.StringJoiner;

/**
 * @author dynmie
 */
public class NowPlayingCommand extends BaseCommand {
    public NowPlayingCommand() {
        super(List.of("nowplaying", "np"));

        setDescription("Get the player to pause");
        setUsage("<client>");

        setMinArgs(1);
        setMaxArgs(1);

        addResolver(0, RemoteClient.class);
    }

    @Override
    public CommandResult onExecute(CommandContext context) {
        RemoteClient client = context.getAt(0, RemoteClient.class);
        PlayerInfo playerInfo = client.getPlayerInfo();

        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("Now playing (" + client.getName() + ")");
        joiner.add("Video: " + playerInfo.getNowPlaying().getTitle() + " (" + playerInfo.getNowPlaying().getVideoId() + ")");
        joiner.add("State: " + (playerInfo.isPaused() ? "Paused" : "Playing"));

        context.sendMessage(joiner.toString());
        return CommandResult.OK;
    }
}
