package me.dynmie.mono.shared.packet;

import com.google.gson.Gson;

import java.util.Objects;

/**
 * @author dynmie
 */
public final class Packets {
    private static final Gson gson = new Gson();

    private Packets() { throw new IllegalStateException("why did you instantiate me bruh"); }

    @SuppressWarnings("unchecked")
    public static RawPacket toRaw(ConnectionState state, PacketDirection direction, Packet<? extends PacketHandler> packet) {
        Objects.requireNonNull(state, "connectionState cannot be null");
        Objects.requireNonNull(direction, "packetDirection cannot be null");
        Objects.requireNonNull(packet, "packet cannot be null");

        int packetId = state.getPacketId(direction, (Class<? extends Packet<? extends PacketHandler>>) packet.getClass());
        String json = gson.toJson(packet);
        return new RawPacket(packetId, json);
    }

    public static Packet<? extends PacketHandler> fromRaw(ConnectionState state, PacketDirection direction, RawPacket rawPacket) {
        Objects.requireNonNull(state, "connectionState cannot be null");
        Objects.requireNonNull(direction, "packetDirection cannot be null");
        Objects.requireNonNull(rawPacket, "rawPacket cannot be null");

        return gson.fromJson(rawPacket.getData(), state.getPacketType(direction, rawPacket.getId()));
    }
}
