package me.dynmie.mono.server.client;

import lombok.Getter;
import lombok.Setter;
import me.dynmie.mono.server.network.connection.ClientConnection;
import me.dynmie.mono.shared.packet.ready.client.*;
import me.dynmie.mono.shared.player.PlayerConfig;
import me.dynmie.mono.shared.player.PlayerInfo;
import me.dynmie.mono.shared.player.PlayerPlaylistInfo;
import me.dynmie.mono.shared.player.PlayerVideoInfo;
import me.dynmie.mono.shared.session.ClientSession;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;

/**
 * @author dynmie
 */
public class RemoteClient {
    private final @Getter ClientSession session;
    private final @Getter ClientConnection connection;
    private final @Getter List<PlayerVideoInfo> queue;
    private @Getter @Setter PlayerInfo playerInfo = null;
    private @Getter PlayerConfig config = new PlayerConfig(true, false, true);

    public RemoteClient(ClientSession session, ClientConnection connection, List<PlayerVideoInfo> queue) {
        this.session = session;
        this.connection = connection;
        this.queue = queue;
    }

    public void setConfig(PlayerConfig config) {
        this.config = config;
        connection.sendPacket(new ClientboundPlayerConfigUpdatePacket(config));
    }

    public void setPlaylistInfo(PlayerPlaylistInfo playlistInfo) {
        queue.clear();
        queue.addAll(playlistInfo.getVideos());
    }

    public void sendPlaylistInfo() {
        connection.sendPacket(new ClientboundPlayerPlaylistUpdatePacket(new PlayerPlaylistInfo(queue)));
    }

    public void addToQueue(PlayerVideoInfo videoInfo) {
        int i = 0;
        while (i < queue.size()) {
            PlayerVideoInfo info = queue.get(i);
            if (info.isDefaultPlaylistVideo()) {
                break;
            }
            i++;
        }
        queue.add(i, videoInfo);
    }

    public void play() {
        connection.sendPacket(new ClientboundPlayerPlayPacket());
    }

    public void pause() {
        connection.sendPacket(new ClientboundPlayerPausePacket());
    }

    public void skip() {
        connection.sendPacket(new ClientboundPlayerSkipPacket());
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
