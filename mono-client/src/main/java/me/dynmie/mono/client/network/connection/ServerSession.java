package me.dynmie.mono.client.network.connection;

import lombok.Getter;

import java.util.UUID;

/**
 * @author dynmie
 */
public class ServerSession {

    private final @Getter String clientName;
    private final @Getter UUID clientUniqueId;

    public ServerSession(String clientName, UUID clientUniqueId) {
        this.clientName = clientName;
        this.clientUniqueId = clientUniqueId;
    }
}
