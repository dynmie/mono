package me.dynmie.mono.server.command.handler.condition;

import me.dynmie.mono.server.command.handler.CommandResult;

/**
 * @author dynmie
 */
public class Conditions {

    private static final CommandCondition emptyCondition = (arg) -> CommandResult.OK;

    public static CommandCondition empty() {
        return emptyCondition;
    }

}
