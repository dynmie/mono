package me.dynmie.mono.server.client.session;

import io.netty.channel.Channel;
import me.dynmie.mono.shared.session.ClientSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dynmie
 */
public class SessionService {
    private final Map<Channel, ClientSession> sessions = new ConcurrentHashMap<>();

    public void removeSessionByChannel(Channel channel) {
        sessions.remove(channel);
    }

    public void addSession(Channel channel, ClientSession session) {
        sessions.put(channel, session);
    }

    public ClientSession getSessionByChannel(Channel channel) {
        return sessions.get(channel);
    }
}
