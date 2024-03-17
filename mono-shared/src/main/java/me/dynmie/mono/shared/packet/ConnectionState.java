package me.dynmie.mono.shared.packet;

import lombok.Getter;
import me.dynmie.mono.shared.packet.exception.PacketIdNotFoundException;
import me.dynmie.mono.shared.packet.exception.PacketTypeNotFoundException;
import me.dynmie.mono.shared.packet.store.PacketStore;
import me.dynmie.mono.shared.packet.store.handshake.ClientboundHandshakePacketStore;
import me.dynmie.mono.shared.packet.store.handshake.ServerboundHandshakePacketStore;
import me.dynmie.mono.shared.packet.store.login.ClientboundLoginPacketStore;
import me.dynmie.mono.shared.packet.store.login.ServerboundLoginPacketStore;
import me.dynmie.mono.shared.packet.store.ready.ClientboundReadyPacketStore;
import me.dynmie.mono.shared.packet.store.ready.ServerboundReadyPacketStore;

import java.util.Map;
import java.util.Objects;

/**
 * @author dynmie
 */
public enum ConnectionState {
    HANDSHAKE(-1, Map.of(
            PacketDirection.SERVERBOUND, new ServerboundHandshakePacketStore(),
            PacketDirection.CLIENTBOUND, new ClientboundHandshakePacketStore()
    )),
    LOGIN(0, Map.of(
            PacketDirection.SERVERBOUND, new ServerboundLoginPacketStore(),
            PacketDirection.CLIENTBOUND, new ClientboundLoginPacketStore()
    )),
    READY(1, Map.of(
            PacketDirection.SERVERBOUND, new ServerboundReadyPacketStore(),
            PacketDirection.CLIENTBOUND, new ClientboundReadyPacketStore()
    ));

    private final @Getter int protocolId;
    private final Map<PacketDirection, PacketStore> packetStores;

    ConnectionState(int protocolId, Map<PacketDirection, PacketStore> packetStores) {
        this.protocolId = protocolId;
        this.packetStores = packetStores;
    }

    public Class<? extends Packet<? extends PacketHandler>> getPacketType(PacketDirection direction, int packetId) {
        Objects.requireNonNull(direction, "packetDirection cannot be null");

        Class<? extends Packet<? extends PacketHandler>> clazz = packetStores.get(direction).get(packetId);
        if (clazz == null) {
            throw new PacketTypeNotFoundException("Packet of ID " + packetId + " in direction " + direction + " not found in " + this);
        }

        return clazz;
    }

    public int getPacketId(PacketDirection direction, Class<? extends Packet<? extends PacketHandler>> packetType) {
        Objects.requireNonNull(direction, "packetDirection cannot be null");
        Objects.requireNonNull(packetType, "packetType cannot be null");

        Integer id = packetStores.get(direction).inverseGet(packetType);
        if (id == null) {
            throw new PacketIdNotFoundException("Packet ID of " + packetType.getName() + " in direction " + direction + " not found in " + this);
        }

        return id;
    }
}
