package me.dynmie.mono.server.command.commands.player.toggle;

import me.dynmie.jeorge.Inject;
import me.dynmie.mono.server.client.ClientHandler;
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

    @Inject
    private ClientHandler clientHandler;

    public ToggleTextDitheringCommand() {
        super(List.of("toggletextdithering", "ttd"));

        setDescription("Get the player to toggle the text dithering state");
        setUsage("<client>");

        setMinArgs(0);
        setMaxArgs(1);

        addResolver(0, RemoteClient.class);
    }

    @Override
    public CommandResult onExecute(CommandContext context) {
        RemoteClient client;
        if (context.size() > 0) {
            client = context.getAt(0, RemoteClient.class);
        } else {
            client = clientHandler.getClient("default");
        }
        if (client == null) {
            return CommandResult.INCORRECT_USAGE;
        }
        PlayerConfig.Mutable mutable = client.getConfig().toMutable();
        mutable.setTextDithering(!mutable.isTextDithering());
        client.setConfig(mutable.toImmutable());
        return CommandResult.OK;
    }
}
