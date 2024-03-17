package me.dynmie.mono.server.network.connection;

import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import me.dynmie.mono.server.Server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dynmie
 */
@RequiredArgsConstructor
public class ConnectionHandler {

    private final Server client;

    private final Map<Channel, ClientConnection> connections = new ConcurrentHashMap<>();

    public void removeConnectionByChannel(Channel channel) {
        connections.remove(channel);
    }

    public void addConnection(ClientConnection connection) {
        connections.put(connection.getChannel(), connection);
    }

    public ClientConnection getConnectionByChannel(Channel channel) {
        return connections.get(channel);
    }

    public ClientConnection createConnection(Channel channel) {
        return new ClientConnection(client.getInjector(), channel);
    }

}
