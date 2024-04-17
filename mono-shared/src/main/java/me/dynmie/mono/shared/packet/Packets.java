package me.dynmie.mono.shared.packet;

import com.google.gson.Gson;

import java.util.Objects;

/**
 * A simple utility class to help with packet management.
 *
 * @see Packet
 * @see RawPacket
 * @author dynmie
 */
public final class Packets {
    private static final Gson gson = new Gson();

    private Packets() { throw new IllegalStateException("why did you instantiate me bruh"); }

    /**
     * Serializes a Packet into a transmittable RawPacket.
     *
     * @see Packet
     * @see RawPacket
     * @param state the current connection state
     * @param direction the direction the packet is travelling
     * @param packet the packet to serialize
     * @return the transmittable RawPacket
     */
    @SuppressWarnings("unchecked")
    public static RawPacket toRaw(ConnectionState state, PacketDirection direction, Packet<? extends PacketHandler> packet) {
        Objects.requireNonNull(state, "connectionState cannot be null");
        Objects.requireNonNull(direction, "packetDirection cannot be null");
        Objects.requireNonNull(packet, "packet cannot be null");

        int packetId = state.getPacketId(direction, (Class<? extends Packet<? extends PacketHandler>>) packet.getClass());
        String json = gson.toJson(packet);
        return new RawPacket(packetId, json);
    }

    /**
     * Deserializes a RawPacket into a readable Packet.
     *
     * @see Packet
     * @see RawPacket
     * @param state the current connection state
     * @param direction the direction the packet is travelling
     * @param rawPacket the packet to deserialize
     * @return the readable Packet
     */
    public static Packet<? extends PacketHandler> fromRaw(ConnectionState state, PacketDirection direction, RawPacket rawPacket) {
        Objects.requireNonNull(state, "connectionState cannot be null");
        Objects.requireNonNull(direction, "packetDirection cannot be null");
        Objects.requireNonNull(rawPacket, "rawPacket cannot be null");

        return gson.fromJson(rawPacket.getData(), state.getPacketType(direction, rawPacket.getId()));
    }
}
