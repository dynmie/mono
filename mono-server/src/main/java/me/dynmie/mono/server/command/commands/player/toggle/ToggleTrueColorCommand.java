package me.dynmie.mono.server.command.commands.player.toggle;

import me.dynmie.mono.server.client.RemoteClient;
import me.dynmie.mono.server.command.handler.BaseCommand;
import me.dynmie.mono.server.command.handler.CommandContext;
import me.dynmie.mono.server.command.handler.CommandResult;
import me.dynmie.mono.shared.player.PlayerConfig;

import java.util.List;

/**
 * @author dynmie
 */
public class ToggleTrueColorCommand extends BaseCommand {
    public ToggleTrueColorCommand() {
        super(List.of("togglefullpixel"));

        setDescription("Get the player to toggle the full pixel state");
        setUsage("<client>");

        setMinArgs(1);
        setMaxArgs(1);

        addResolver(0, RemoteClient.class);
    }

    @Override
    public CommandResult onExecute(CommandContext context) {
        RemoteClient client = context.getAt(0, RemoteClient.class);
        PlayerConfig.Mutable mutable = client.getConfig().toMutable();
        mutable.setFullPixel(!mutable.isFullPixel());
        client.setConfig(mutable.toImmutable());
        return CommandResult.OK;
    }
}
