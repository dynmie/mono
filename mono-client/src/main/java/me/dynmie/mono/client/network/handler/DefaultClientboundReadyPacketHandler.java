package me.dynmie.mono.client.network.handler;

import me.dynmie.jeorge.Inject;
import me.dynmie.jeorge.Injector;
import me.dynmie.mono.client.QClient;
import me.dynmie.mono.client.network.connection.ServerConnection;
import me.dynmie.mono.client.player.PlayerController;
import me.dynmie.mono.client.player.VideoPlayer;
import me.dynmie.mono.client.queue.ProperVideoInfo;
import me.dynmie.mono.client.queue.QueueService;
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
    private PlayerController playerController;

    @Inject
    private QueueService queueService;

    public DefaultClientboundReadyPacketHandler(Injector injector, ServerConnection connection) {
        this.injector = injector;
        this.connection = connection;

        injector.injectMembers(this);
    }

    @Override
    public void onPlayerPlay(ClientboundPlayerPlayPacket packet) {
        VideoPlayer player = playerController.getPlayer();
        if (player == null || !player.isRunning()) {
            queueService.sendQueueUpdate();
            return;
        }
        player.play();
        ProperVideoInfo nowPlaying = queueService.getQueue().getNowPlaying();
        if (nowPlaying == null) return;
        connection.sendPacket(new ServerboundPlayerInfoPacket(
                new PlayerInfo(nowPlaying.getInfo(), player.isPaused())
        ));
    }

    @Override
    public void onPlayerPause(ClientboundPlayerPausePacket packet) {
        VideoPlayer player = playerController.getPlayer();
        if (player == null) {
            return;
        }
        player.pause();
        ProperVideoInfo nowPlaying = queueService.getQueue().getNowPlaying();
        if (nowPlaying == null) return;
        connection.sendPacket(new ServerboundPlayerInfoPacket(
                new PlayerInfo(nowPlaying.getInfo(), player.isPaused())
        ));
    }

    @Override
    public void onSkip(ClientboundPlayerSkipPacket packet) {
        VideoPlayer player = playerController.getPlayer();
        if (player == null) {
            return;
        }
        player.stop();
    }

    @Override
    public void onPlaylistUpdate(ClientboundPlayerPlaylistUpdatePacket packet) {
        List<PlayerVideoInfo> newQueue = packet.getPlaylistInfo().getVideos();

        queueService.getQueue().updateQueue(newQueue);

        queueService.sendQueueUpdate();
    }

    @Override
    public void onPlayerConfigUpdate(ClientboundPlayerConfigUpdatePacket packet) {
        playerController.setConfig(packet.getConfig());
    }
}
