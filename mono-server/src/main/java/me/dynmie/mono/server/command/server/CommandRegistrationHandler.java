package me.dynmie.mono.server.command.server;

import lombok.AllArgsConstructor;
import me.dynmie.jeorge.Injector;
import me.dynmie.mono.server.command.commands.ServerHelpCommand;
import me.dynmie.mono.server.command.commands.ShutdownCommand;
import me.dynmie.mono.server.command.commands.clients.ClientInfoCommand;
import me.dynmie.mono.server.command.commands.clients.ClientsCommand;
import me.dynmie.mono.server.command.commands.player.*;
import me.dynmie.mono.server.command.commands.player.queue.QueueCommand;
import me.dynmie.mono.server.command.commands.player.toggle.ToggleColorCommand;
import me.dynmie.mono.server.command.commands.player.toggle.ToggleTextDitheringCommand;
import me.dynmie.mono.server.command.commands.player.toggle.ToggleTrueColorCommand;
import me.dynmie.mono.server.command.handler.BaseCommand;
import me.dynmie.mono.server.command.handler.CommandHandler;

import java.util.List;

/**
 * @author dynmie
 */
@AllArgsConstructor
public class CommandRegistrationHandler {
    private static final List<Class<? extends BaseCommand>> commandTypes = List.of(
            ServerHelpCommand.class,
            PauseCommand.class,
            PlayCommand.class,
            SkipCommand.class,
            ClientInfoCommand.class,
            ClientsCommand.class,
            NowPlayingCommand.class,
            QueueCommand.class,
            ShutdownCommand.class,
            ToggleColorCommand.class,
            ToggleTrueColorCommand.class,
            ToggleTextDitheringCommand.class
    );

    private final Injector injector;
    private final CommandHandler commandHandler;

    public void initialize() {
        commandTypes.stream().map(injector::createInstance).forEach(commandHandler::register);
    }
}
