package me.dynmie.mono.server.command.handler.resolver.resolvers;

import me.dynmie.mono.server.command.handler.CommandContext;
import me.dynmie.mono.server.command.handler.CommandResult;
import me.dynmie.mono.server.command.handler.resolver.ArgumentResolver;
import me.dynmie.mono.server.command.handler.resolver.UnresolvedException;

/**
 * @author dynmie
 */
public class BooleanResolver implements ArgumentResolver<Boolean> {
    @Override
    public Boolean resolve(CommandContext context, String arg) {
        if (arg.equalsIgnoreCase("false")) {
            return false;
        } else if (arg.equalsIgnoreCase("true")) {
            return true;
        }
        throw new UnresolvedException();
    }

    @Override
    public CommandResult failed(CommandContext context) {
        return CommandResult.INCORRECT_USAGE;
    }
}
