package me.dynmie.mono.server.command.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dynmie
 */
@Getter
@AllArgsConstructor
public enum CommandResult {
    OK(true),
    INCORRECT_USAGE(false),
    SILENT_FAIL(false);

    private final boolean success;
}


