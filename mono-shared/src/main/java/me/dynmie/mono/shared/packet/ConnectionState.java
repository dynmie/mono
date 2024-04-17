package me.dynmie.mono.shared.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.dynmie.mono.shared.packet.exception.PacketIdNotFoundException;
import me.dynmie.mono.shared.packet.exception.PacketTypeNotFoundException;
import me.dynmie.mono.shared.packet.handshake.ClientboundHandshakePacketStore;
import me.dynmie.mono.shared.packet.handshake.ServerboundHandshakePacketStore;
import me.dynmie.mono.shared.packet.login.ClientboundLoginPacketStore;
import me.dynmie.mono.shared.packet.login.ServerboundLoginPacketStore;
import me.dynmie.mono.shared.packet.ready.ClientboundReadyPacketStore;
import me.dynmie.mono.shared.packet.ready.ServerboundReadyPacketStore;

import java.util.Map;
import java.util.Objects;

/**
 * Represents the different states of connection during packet transmission.
 * Each state holds a set of packet stores for both clientbound and serverbound directions.
 *
 * @author dynmie
 */
@AllArgsConstructor
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

    /**
     * Retrieves the type of packet corresponding to the given packet direction and identifier.
     *
     * @param direction the direction the packet is travelling
     * @param packetId the identifier of the packet
     * @return the type of packet
     * @throws PacketTypeNotFoundException if the packet type is not found in the specified direction
     */
    public Class<? extends Packet<? extends PacketHandler>> getPacketType(PacketDirection direction, int packetId) {
        Objects.requireNonNull(direction, "packetDirection cannot be null");

        Class<? extends Packet<? extends PacketHandler>> clazz = packetStores.get(direction).get(packetId);
        if (clazz == null) {
            throw new PacketTypeNotFoundException("Packet of ID " + packetId + " in direction " + direction + " not found in " + this);
        }

        return clazz;
    }

    /**
     * Retrieves the identifier of the packet corresponding to the given direction and packet type.
     *
     * @param direction the direction the packet is travelling
     * @param packetType the type of packet
     * @return the identifier of the packet
     * @throws PacketIdNotFoundException if the packet identifier is not found for the specified type and direction
     */
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
