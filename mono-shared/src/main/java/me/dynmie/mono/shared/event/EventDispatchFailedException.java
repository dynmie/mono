package me.dynmie.mono.shared.event;

/**
 * @author dynmie
 */
public class EventDispatchFailedException extends RuntimeException {
    EventDispatchFailedException(String message) {
        super(message);
    }

    EventDispatchFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
