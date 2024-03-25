package me.dynmie.mono.server.command.handler.resolver;

/**
 * @author dynmie
 */
public class ExistingResolvingTypeException extends RuntimeException {
    public ExistingResolvingTypeException() {
        super();
    }

    public ExistingResolvingTypeException(String message) {
        super(message);
    }


    public ExistingResolvingTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistingResolvingTypeException(Throwable cause) {
        super(cause);
    }

    protected ExistingResolvingTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
