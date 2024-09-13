package me.dynmie.mono.client.network.handler;

import me.dynmie.jeorge.Inject;
import me.dynmie.jeorge.Injector;
import me.dynmie.mono.client.QClient;
import me.dynmie.mono.client.network.connection.ServerConnection;
import me.dynmie.mono.client.player.PlayerHandler;
import me.dynmie.mono.client.player.ProperVideoInfo;
import me.dynmie.mono.client.player.QueueHandler;
import me.dynmie.mono.client.player.VideoPlayer;
import me.dynmie.mono.shared.packet.ready.ClientboundReadyPacketHandler;
import me.dynmie.mono.shared.packet.ready.client.*;
import me.dynmie.mono.shared.packet.ready.server.ServerboundPlayerInfoPacket;
import me.dynmie.mono.shared.player.PlayerInfo;
import me.dynmie.mono.shared.player.PlayerVideoInfo;

import java.util.List;

/**
 * @author dynmie
 */
public class DefaultClientboundReadyPacketHandler implements ClientboundReadyPacketHandler {

    private final Injector injector;
    private final ServerConnection connection;

    @Inject
    private QClient client;

    @Inject
    private PlayerHandler playerHandler;

    @Inject
    private QueueHandler queueHandler;

    public DefaultClientboundReadyPacketHandler(Injector injector, ServerConnection connection) {
        this.injector = injector;
        this.connection = connection;

        injector.injectMembers(this);
    }

    @Override
    public void onPlayerPlay(ClientboundPlayerPlayPacket packet) {
        VideoPlayer player = playerHandler.getPlayer();
        if (player == null || !player.isRunning()) {
            queueHandler.knockQueue();
            return;
        }
        player.play();
        ProperVideoInfo nowPlaying = queueHandler.getQueue().getNowPlaying();
        if (nowPlaying == null) return;
        connection.sendPacket(new ServerboundPlayerInfoPacket(
                new PlayerInfo(nowPlaying.getInfo(), player.isPaused())
        ));
    }

    @Override
    public void onPlayerPause(ClientboundPlayerPausePacket packet) {
        VideoPlayer player = playerHandler.getPlayer();
        if (player == null) {
            return;
        }
        player.pause();
        ProperVideoInfo nowPlaying = queueHandler.getQueue().getNowPlaying();
        if (nowPlaying == null) return;
        connection.sendPacket(new ServerboundPlayerInfoPacket(
                new PlayerInfo(nowPlaying.getInfo(), player.isPaused())
        ));
    }

    @Override
    public void onSkip(ClientboundPlayerSkipPacket packet) {
        VideoPlayer player = playerHandler.getPlayer();
        if (player == null) {
            return;
        }
        player.stop();
    }

    @Override
    public void onPlaylistUpdate(ClientboundPlayerPlaylistUpdatePacket packet) {
        List<PlayerVideoInfo> newQueue = packet.getPlaylistInfo().getVideos();

        queueHandler.getQueue().updateQueue(newQueue);

        queueHandler.knockQueue();
    }

    @Override
    public void onPlayerConfigUpdate(ClientboundPlayerConfigUpdatePacket packet) {
        playerHandler.setConfig(packet.getConfig());
    }
}
