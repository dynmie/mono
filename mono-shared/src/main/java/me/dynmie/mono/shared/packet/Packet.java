package me.dynmie.mono.shared.packet;

/**
 * A generic interface representing a packet that will be handled by a packet handler.
 *
 * @see PacketHandler
 * @param <T> the type of packet handler that will handle this packet
 * @author dynmie
 */
public interface Packet<T extends PacketHandler> {
    /**
     * Handles this packet using the provided packet handler.
     *
     * @param handler the packet handler that will handle this packet
     */
    void handle(T handler);
}
