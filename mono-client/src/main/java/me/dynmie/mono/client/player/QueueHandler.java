package me.dynmie.mono.client.player;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.VideoFormat;
import lombok.Getter;
import lombok.Setter;
import me.dynmie.mono.client.QClient;
import me.dynmie.mono.client.network.NetworkHandler;
import me.dynmie.mono.shared.packet.ready.server.ServerboundPlayerPlaylistUpdatePacket;
import me.dynmie.mono.shared.player.PlayerPlaylistInfo;
import me.dynmie.mono.shared.player.PlayerVideoInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dynmie
 */
@Setter
@Getter
public class QueueHandler {
    private final YoutubeDownloader downloader = new YoutubeDownloader();

    private final QClient client;
    private final NetworkHandler networkHandler;

    private List<PlayerVideoInfo> queue = new ArrayList<>();
    private ActualVideoInfo nextVideo;
    private ActualVideoInfo nowPlaying;

    private Thread thread;

    private final File outputDirectory;

    public QueueHandler(QClient client, NetworkHandler networkHandler) {
        this.client = client;
        this.networkHandler = networkHandler;

        this.outputDirectory = new File(client.getWorkingFolder(), "videos");
    }

    public void initialize() {

    }

    public void update(boolean override) {
        if (nextVideo != null && !override) return;
        if (thread != null) return;

        thread = Thread.startVirtualThread(() -> {
            if (queue.isEmpty()) return;
            PlayerVideoInfo first = queue.getFirst();

            RequestVideoInfo requestVideoInfo = new RequestVideoInfo(first.getVideoId());
            VideoInfo videoInfo = downloader.getVideoInfo(requestVideoInfo).data();
            VideoFormat videoFormat = videoInfo.bestVideoWithAudioFormat();

            String fileName = first.getVideoId();
            File shouldFile = new File(outputDirectory, fileName + videoFormat.extension().value());

            if (shouldFile.exists()) {
                nextVideo = new ActualVideoInfo(shouldFile, first);
            } else {
                RequestVideoFileDownload request = new RequestVideoFileDownload(videoFormat)
                        .saveTo(outputDirectory)
                        .renameTo(fileName);

                File output = downloader.downloadVideoFile(request).data();

                nextVideo = new ActualVideoInfo(output, first);
            }

            networkHandler.getConnection().sendPacket(new ServerboundPlayerPlaylistUpdatePacket(
                    new PlayerPlaylistInfo(queue)
            ));

            synchronized (PlayerHandler.LOCK) {
                PlayerHandler.LOCK.notify();
            }

            thread = null;
        });
    }

    public void next() {
        if (queue.isEmpty()) {
            return;
        }
        PlayerVideoInfo first = queue.removeFirst();

        if (first.isDefaultPlaylistVideo()) {
            queue.addLast(first);
        }
    }
}
