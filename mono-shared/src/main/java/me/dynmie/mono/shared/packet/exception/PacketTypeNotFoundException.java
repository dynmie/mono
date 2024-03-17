package me.dynmie.mono.shared.packet.exception;

/**
 * @author dynmie
 */
public class PacketTypeNotFoundException extends PacketFindException {
    public PacketTypeNotFoundException() {
        super();
    }

    public PacketTypeNotFoundException(String message) {
        super(message);
    }

    public PacketTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PacketTypeNotFoundException(Throwable cause) {
        super(cause);
    }

    protected PacketTypeNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
