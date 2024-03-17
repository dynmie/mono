package me.dynmie.mono.shared.packet;

import lombok.Getter;

/**
 * @author dynmie
 */
@Getter
public abstract class Packet<T extends PacketHandler> {
    public abstract void handle(T handler);
}
