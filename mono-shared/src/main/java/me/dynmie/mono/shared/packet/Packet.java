package me.dynmie.mono.shared.packet;

/**
 * @author dynmie
 */
public interface Packet<T extends PacketHandler> {
    void handle(T handler);
}
