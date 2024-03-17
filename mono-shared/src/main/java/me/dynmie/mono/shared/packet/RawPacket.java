package me.dynmie.mono.shared.packet;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author dynmie
 */
@Getter
public class RawPacket implements Serializable {
    private final int id;
    private final String data;

    public RawPacket(int id, String data) {
        this.id = id;
        this.data = data;
    }
}
