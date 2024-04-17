package me.dynmie.mono.shared.packet;

import lombok.Value;

import java.io.Serializable;

/**
 * A transmittable, immutable packet containing the packet id and the raw JSON data as a string.
 *
 * @see Packet
 * @author dynmie
 */
@Value
public class RawPacket implements Serializable {
    int id;
    String data;
}
