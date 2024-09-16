package me.dynmie.mono.client.queue;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.VideoFormat;
import lombok.Getter;
import me.dynmie.mono.client.QClient;
import me.dynmie.mono.client.network.NetworkHandler;
import me.dynmie.mono.client.network.connection.ServerConnection;
import me.dynmie.mono.client.player.PlayerController;
import me.dynmie.mono.shared.packet.ConnectionState;
import me.dynmie.mono.shared.packet.ready.server.ServerboundPlayerPlaylistUpdatePacket;
import me.dynmie.mono.shared.player.PlayerPlaylistInfo;
import me.dynmie.mono.shared.player.PlayerVideoInfo;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author dynmie
 */
public class QueueService {
    private final YoutubeDownloader downloader = new YoutubeDownloader();

    private final NetworkHandler networkHandler;

    private final @Getter Queue queue = new Queue();
    private final @Getter File outputDirectory;

    public QueueService(QClient client, NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;

        this.outputDirectory = new File(client.getWorkingFolderPath().toFile(), "videos");
    }

    public CompletableFuture<Void> downloadVideo(ProperVideoInfo properInfo) {
        return CompletableFuture.runAsync(() -> {
            if (properInfo.isTaskTaken()) return;
            properInfo.setTaskTaken(true);

            PlayerVideoInfo playerInfo = properInfo.getInfo();

            RequestVideoInfo requestVideoInfo = new RequestVideoInfo(playerInfo.getVideoId());

            VideoInfo videoInfo;
            try {
                videoInfo = downloader.getVideoInfo(requestVideoInfo).data(30, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                properInfo.setSuccess(false);
                properInfo.setFinished(true);
                return;
            }
            if (!videoInfo.details().isDownloadable()) {
                properInfo.setSuccess(false);
                properInfo.setFinished(true);
                return;
            }

            VideoFormat videoFormat = videoInfo.bestVideoWithAudioFormat();

            File shouldFile = new File(outputDirectory, playerInfo.getVideoId() + "." + videoFormat.extension().value());

            if (shouldFile.exists()) {
                properInfo.setFile(shouldFile);
                properInfo.setSuccess(true);
                properInfo.setFinished(true);
            } else {
                RequestVideoFileDownload request = new RequestVideoFileDownload(videoFormat)
                        .saveTo(outputDirectory)
                        .renameTo(playerInfo.getVideoId());

                File output;
                try {
                    output = downloader.downloadVideoFile(request).data(2, TimeUnit.MINUTES);
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                }

                properInfo.setFile(output);
                properInfo.setFinished(true);
                properInfo.setSuccess(true);
            }

        });
    }

    public void onVideoPlay() {
        prepareNextVideo();
    }

    public void knockQueue() {
        synchronized (PlayerController.LOCK) {
            PlayerController.LOCK.notify();
        }

        prepareNextVideo();
    }

    private void prepareNextVideo() {
        ProperVideoInfo nextVideo = queue.getNextVideo();
        if (nextVideo == null) {
            return;
        }

        downloadVideo(nextVideo).thenAccept(v -> knockQueue());
    }

    public void onVideoPrePlay() {
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
            downloadVideo(nowPlaying).thenAccept(v -> knockQueue());
        }
    }

    public void onVideoEnd() {
        queue.next();
    }

}
