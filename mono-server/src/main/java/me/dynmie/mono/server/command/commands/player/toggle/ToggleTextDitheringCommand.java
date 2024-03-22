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
public class ToggleTextDitheringCommand extends BaseCommand {
    public ToggleTextDitheringCommand() {
        super(List.of("toggletextdithering"));

        setDescription("Get the player to toggle the text dithering state");
        setUsage("<client>");

        setMinArgs(1);
        setMaxArgs(1);

        addResolver(0, RemoteClient.class);
    }

    @Override
    public CommandResult onExecute(CommandContext context) {
        RemoteClient client = context.getAt(0, RemoteClient.class);
        PlayerConfig.Mutable mutable = client.getConfig().toMutable();
        mutable.setTextDithering(!mutable.isTextDithering());
        client.setConfig(mutable.toImmutable());
        return CommandResult.OK;
    }
}
