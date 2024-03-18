package me.dynmie.mono.server.command.handler.condition;

import me.dynmie.mono.server.command.handler.CommandResult;

/**
 * @author dynmie
 */
public interface CommandCondition {

    /**
     * Forces an argument to have a certain condition before executing the command.
     * @param arg Argument to check for
     * @return Status of the check
     */
    CommandResult check(String arg);

}
