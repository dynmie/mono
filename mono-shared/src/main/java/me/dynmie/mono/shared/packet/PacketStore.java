package me.dynmie.mono.shared.packet;

import me.dynmie.mono.shared.store.Store;

/**
 * @author dynmie
 */
public abstract class PacketStore extends Store<Integer, Class<? extends Packet<? extends PacketHandler>>> {

}
