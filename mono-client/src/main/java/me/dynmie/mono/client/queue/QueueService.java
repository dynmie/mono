package me.dynmie.mono.client.queue;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.client.ClientType;
import com.github.kiulian.downloader.downloader.client.Clients;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.VideoFormat;
import lombok.Getter;
import me.dynmie.mono.shared.player.PlayerVideoInfo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author dynmie
 */
public class QueueService {
    private final YoutubeDownloader downloader = new YoutubeDownloader();

    public static final Object QUEUE_UPDATE_LOCK = new Object();

    private final @Getter Queue queue = new Queue();
    private final @Getter Path outputDirectoryPath;

    public QueueService(Path workingFolderPath) {
        this.outputDirectoryPath = workingFolderPath.resolve("videos");
    }

    static {
        Clients.setHighestPriorityClientType(ClientType.WEB_PARENT_TOOLS);
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

            Path shouldPath = outputDirectoryPath.resolve("%s.%s".formatted(
                    playerInfo.getVideoId(),
                    videoFormat.extension().value()
            ));

            if (Files.exists(shouldPath)) {
                properInfo.setPath(shouldPath);
                properInfo.setSuccess(true);
                properInfo.setFinished(true);
            } else {
                RequestVideoFileDownload request = new RequestVideoFileDownload(videoFormat)
                        .saveTo(outputDirectoryPath.toFile())
                        .renameTo(playerInfo.getVideoId());

                Path output;
                try {
                    output = downloader.downloadVideoFile(request).data(2, TimeUnit.MINUTES).toPath();
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                }

                properInfo.setPath(output);
                properInfo.setFinished(true);
                properInfo.setSuccess(true);
            }

        });
    }

    public void sendQueueUpdate() {
        synchronized (QUEUE_UPDATE_LOCK) {
            QUEUE_UPDATE_LOCK.notify();
        }

        prepareNextVideo();
    }

    public void prepareNextVideo() {
        ProperVideoInfo nextVideo = queue.getNextVideo();
        if (nextVideo == null) {
            return;
        }

        downloadVideo(nextVideo).thenAccept(v -> sendQueueUpdate());
    }

}
