package me.dynmie.mono.shared.packet;

/**
 * An enum that represents the direction a packet is travelling.
 * @author dynmie
 */
public enum PacketDirection {

    /**
     * Represents packets being sent from the client to the server.
     */
    SERVERBOUND,

    /**
     * Represents packets being sent from the server to the client.
     */
    CLIENTBOUND
}
