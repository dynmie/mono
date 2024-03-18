package me.dynmie.mono.server.client;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dynmie
 */
public class ClientHandler {
    private final Map<UUID, RemoteClient> clients = new ConcurrentHashMap<>();

    public RemoteClient getClient(UUID uniqueId) {
        return clients.get(uniqueId);
    }

    public RemoteClient getClient(String name) {
        return clients.values().stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public void addClient(RemoteClient client) {
        clients.put(client.getUniqueId(), client);
    }

    public void removeClient(RemoteClient client) {
        clients.remove(client.getUniqueId());
    }
}
