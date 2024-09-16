package me.dynmie.mono.shared.event;

/**
 * @author dynmie
 */
public class NotAnEventException extends RuntimeException {
    NotAnEventException(String message) {
        super(message);
    }
}
