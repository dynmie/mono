package me.dynmie.mono.client.queue;

import lombok.SneakyThrows;
import me.dynmie.mono.client.network.NetworkHandler;
import me.dynmie.mono.client.network.connection.ServerConnection;
import me.dynmie.mono.client.player.PlayerController;
import me.dynmie.mono.client.player.VideoPlayer;
import me.dynmie.mono.shared.packet.ConnectionState;
import me.dynmie.mono.shared.packet.ready.server.ServerboundPlayerInfoPacket;
import me.dynmie.mono.shared.packet.ready.server.ServerboundPlayerPlaylistUpdatePacket;
import me.dynmie.mono.shared.player.PlayerInfo;
import me.dynmie.mono.shared.player.PlayerPlaylistInfo;

/**
 * @author dynmie
 */
public class QueueHandler {

    private final NetworkHandler networkHandler;
    private final QueueService queueService;
    private final PlayerController playerController;

    private final Queue queue;

    private Thread queueThread;

    public QueueHandler(NetworkHandler networkHandler, QueueService queueService, PlayerController playerController) {
        this.networkHandler = networkHandler;
        this.playerController = playerController;
        this.queueService = queueService;

        this.queue = queueService.getQueue();
    }

    public void initialize() {
        queueThread = Thread.startVirtualThread(this::startQueue);
    }

    @SneakyThrows
    public void shutdown() {
        queueThread.interrupt();
        playerController.stop();
        queueThread.join();
    }

    @SneakyThrows
    private void startQueue() {
        while (!Thread.interrupted()) {
            onVideoPrePlay();

            ProperVideoInfo nowPlaying = queueService.getQueue().getNowPlaying();
            if (nowPlaying == null || !nowPlaying.isSuccess()) {
                synchronized (QueueService.QUEUE_UPDATE_LOCK) {
                    QueueService.QUEUE_UPDATE_LOCK.wait();
                }
                continue;
            }

            onVideoPlay();
            playerController.readyFor(nowPlaying.getPath());
            playerController.play();

            VideoPlayer player = playerController.getPlayer();

            networkHandler.getConnection().sendPacket(new ServerboundPlayerInfoPacket(
                    new PlayerInfo(nowPlaying.getInfo(), player.isPaused())
            ));

            player.awaitFinish();

            onVideoEnd();
        }

    }

    private void onVideoPlay() {
        queueService.prepareNextVideo();
    }

    private void onVideoPrePlay() {
        ServerConnection connection = networkHandler.getConnection();
        if (connection != null && connection.getConnectionState() == ConnectionState.READY) {
            connection.sendPacket(new ServerboundPlayerPlaylistUpdatePacket(
                    new PlayerPlaylistInfo(queue.toPlayerVideoInfoList())
            ));
        }

        if (queue.getNowPlaying() == null || queue.getNowPlaying().isFailed()) {
            queue.next();
        }

        ProperVideoInfo nowPlaying = queue.getNowPlaying();
        if (nowPlaying == null) return;

        if (!nowPlaying.isTaskTaken()) {
            queueService.downloadVideo(nowPlaying).thenAccept(v -> queueService.sendQueueUpdate());
        }
    }

    private void onVideoEnd() {
        queue.next();
    }

}
