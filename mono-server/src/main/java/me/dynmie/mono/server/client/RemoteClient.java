package me.dynmie.mono.server.client;

import lombok.Getter;
import me.dynmie.mono.server.network.connection.ClientConnection;
import me.dynmie.mono.shared.packet.ready.client.ClientboundPlayerPausePacket;
import me.dynmie.mono.shared.packet.ready.client.ClientboundPlayerPlayPacket;
import me.dynmie.mono.shared.session.ClientSession;

import java.net.InetSocketAddress;
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

    public void play() {
        connection.sendPacket(new ClientboundPlayerPlayPacket());
    }

    public void pause() {
        connection.sendPacket(new ClientboundPlayerPausePacket());
    }

    public InetSocketAddress getAddress() {
        return (InetSocketAddress) connection.getChannel().remoteAddress();
    }

    public boolean isConnected() {
        return connection.isConnected();
    }

    public String getName() {
        return session.getName();
    }

    public UUID getUniqueId() {
        return session.getUniqueId();
    }
}
