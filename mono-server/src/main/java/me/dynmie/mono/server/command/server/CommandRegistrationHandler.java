package me.dynmie.mono.server.command.server;

import lombok.AllArgsConstructor;
import me.dynmie.jeorge.Injector;
import me.dynmie.mono.server.command.commands.ServerHelpCommand;
import me.dynmie.mono.server.command.handler.BaseCommand;
import me.dynmie.mono.server.command.handler.CommandHandler;

import java.util.List;

/**
 * @author dynmie
 */
@AllArgsConstructor
public class CommandRegistrationHandler {
    private static final List<Class<? extends BaseCommand>> commandTypes = List.of(
            ServerHelpCommand.class
    );

    private final Injector injector;
    private final CommandHandler commandHandler;

    public void initialize() {
        commandTypes.stream().map(injector::createInstance).forEach(commandHandler::register);
    }
}
