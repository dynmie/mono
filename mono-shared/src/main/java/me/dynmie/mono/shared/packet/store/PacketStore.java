package me.dynmie.mono.shared.packet.store;

import me.dynmie.mono.shared.packet.Packet;
import me.dynmie.mono.shared.packet.PacketHandler;
import me.dynmie.mono.shared.store.Store;

/**
 * @author dynmie
 */
public abstract class PacketStore extends Store<Integer, Class<? extends Packet<? extends PacketHandler>>> {
}
