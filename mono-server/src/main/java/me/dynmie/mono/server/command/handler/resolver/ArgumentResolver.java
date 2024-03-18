package me.dynmie.mono.server.command.handler.resolver;

import me.dynmie.mono.server.command.handler.CommandContext;
import me.dynmie.mono.server.command.handler.CommandResult;

/**
 * @author dynmie
 */
public interface ArgumentResolver<T> {
    T resolve(CommandContext context, String arg);

    CommandResult failed(CommandContext context);
}
