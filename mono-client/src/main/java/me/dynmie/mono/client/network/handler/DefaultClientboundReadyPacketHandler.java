package me.dynmie.mono.client.network.handler;

import me.dynmie.jeorge.Inject;
import me.dynmie.jeorge.Injector;
import me.dynmie.mono.client.QClient;
import me.dynmie.mono.client.network.connection.ServerConnection;
import me.dynmie.mono.client.player.ActualVideoInfo;
import me.dynmie.mono.client.player.PlayerHandler;
import me.dynmie.mono.client.player.QueueHandler;
import me.dynmie.mono.client.player.VideoPlayer;
import me.dynmie.mono.shared.packet.ready.ClientboundReadyPacketHandler;
import me.dynmie.mono.shared.packet.ready.client.ClientboundPlayerPausePacket;
import me.dynmie.mono.shared.packet.ready.client.ClientboundPlayerPlayPacket;
import me.dynmie.mono.shared.packet.ready.client.ClientboundPlayerPlaylistUpdatePacket;
import me.dynmie.mono.shared.packet.ready.client.ClientboundPlayerSkipPacket;
import me.dynmie.mono.shared.packet.ready.server.ServerboundPlayerInfoPacket;
import me.dynmie.mono.shared.packet.ready.server.ServerboundPlayerPlaylistUpdatePacket;
import me.dynmie.mono.shared.player.PlayerInfo;
import me.dynmie.mono.shared.player.PlayerPlaylistInfo;
import me.dynmie.mono.shared.player.PlayerVideoInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            queueHandler.update(true);
            return;
        }
        player.play();
        ActualVideoInfo nowPlaying = queueHandler.getNowPlaying();
        if (nowPlaying == null) return;
        connection.sendPacket(new ServerboundPlayerInfoPacket(
                new PlayerInfo(nowPlaying.info(), player.isPaused())
        ));
    }

    @Override
    public void onPlayerPause(ClientboundPlayerPausePacket packet) {
        VideoPlayer player = playerHandler.getPlayer();
        if (player == null) {
            return;
        }
        player.pause();
        ActualVideoInfo nowPlaying = queueHandler.getNowPlaying();
        if (nowPlaying == null) return;
        connection.sendPacket(new ServerboundPlayerInfoPacket(
                new PlayerInfo(nowPlaying.info(), player.isPaused())
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
        List<PlayerVideoInfo> prevQueue = queueHandler.getQueue();
        PlayerVideoInfo prevFirst = null;
        if (!prevQueue.isEmpty()) {
            prevFirst = prevQueue.getFirst();
        }

        List<PlayerVideoInfo> newQueue = packet.getPlaylistInfo().getVideos();
        PlayerVideoInfo newFirst = null;
        if (!newQueue.isEmpty()) {
            newFirst = newQueue.getFirst();
        }

        queueHandler.setQueue(new ArrayList<>(packet.getPlaylistInfo().getVideos()));

        if (prevFirst != newFirst) {
            Thread.startVirtualThread(() -> {
                Optional.ofNullable(queueHandler.getThread()).ifPresent(t -> {
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                queueHandler.update(true);
            });
        }

        connection.sendPacket(new ServerboundPlayerPlaylistUpdatePacket(
                new PlayerPlaylistInfo(queueHandler.getQueue())
        ));
    }
}
