package me.dynmie.mono.server.client;

import lombok.Getter;
import me.dynmie.mono.server.network.connection.ClientConnection;
import me.dynmie.mono.shared.session.ClientSession;

import java.net.SocketAddress;
import java.util.UUID;

/**
 * @author dynmie
 */
public class RemoteClient {
    private final @Getter ClientSession session;
    private final @Getter ClientConnection connection;

    public RemoteClient(ClientSession session, ClientConnection connection) {
        this.session = session;
        this.connection = connection;
    }

    public SocketAddress getAddress() {
        return connection.getChannel().remoteAddress();
    }

    public boolean isConnected() {
        return connection.isConnected();
    }

    public UUID getUniqueId() {
        return session.getUniqueId();
    }
}
