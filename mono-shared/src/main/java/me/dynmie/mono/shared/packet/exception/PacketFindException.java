package me.dynmie.mono.shared.packet.exception;

/**
 * @author dynmie
 */
public class PacketFindException extends RuntimeException {
    public PacketFindException() {
        super();
    }

    public PacketFindException(String message) {
        super(message);
    }

    public PacketFindException(String message, Throwable cause) {
        super(message, cause);
    }

    public PacketFindException(Throwable cause) {
        super(cause);
    }

    protected PacketFindException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
