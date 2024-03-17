package me.dynmie.mono.shared.packet.exception;

/**
 * @author dynmie
 */
public class PacketIdNotFoundException extends PacketFindException {
    public PacketIdNotFoundException() {
        super();
    }

    public PacketIdNotFoundException(String message) {
        super(message);
    }

    public PacketIdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PacketIdNotFoundException(Throwable cause) {
        super(cause);
    }

    protected PacketIdNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
