package me.dynmie.mono.server.command.handler.resolver;

/**
 * @author dynmie
 */
public class ResolverNotFoundException extends RuntimeException {

    public ResolverNotFoundException() {
        super();
    }

    public ResolverNotFoundException(String message) {
        super(message);
    }


    public ResolverNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResolverNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ResolverNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
