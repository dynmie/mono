package me.dynmie.mono.server.player;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.client.ClientType;
import com.github.kiulian.downloader.downloader.client.Clients;
import com.github.kiulian.downloader.downloader.request.RequestPlaylistInfo;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.playlist.PlaylistInfo;
import com.github.kiulian.downloader.model.playlist.PlaylistVideoDetails;
import com.github.kiulian.downloader.model.videos.VideoDetails;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.dynmie.mono.server.data.ServerConfig;
import me.dynmie.mono.shared.player.PlayerPlaylistInfo;
import me.dynmie.mono.shared.player.PlayerVideoInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author dynmie
 */
@RequiredArgsConstructor
public class VideoService {

    private final ServerConfig.PlayerConfiguration configuration;
    private final Logger logger;
    private final YoutubeDownloader downloader = new YoutubeDownloader();
    private List<PlayerVideoInfo> infos = new ArrayList<>();

    static {
        Clients.setHighestPriorityClientType(ClientType.WEB_PARENT_TOOLS);
    }

    @SneakyThrows
    public void initialize() {
        String playlistId = configuration.getDefaultPlaylistId();
        if (playlistId.isEmpty() || playlistId.isBlank()) {
            logger.info("No playlist loaded.");
            return;
        }

        logger.info("Loading playlist '" + playlistId + "'...");
        RequestPlaylistInfo request = new RequestPlaylistInfo(playlistId);
        Response<PlaylistInfo> response = downloader.getPlaylistInfo(request);
        PlaylistInfo playlistInfo = response.data(30, TimeUnit.SECONDS);

        List<PlayerVideoInfo> infos = new ArrayList<>();
        for (PlaylistVideoDetails video : playlistInfo.videos()) {
            infos.add(
                    new PlayerVideoInfo(true, video.title(), video.videoId())
            );
        }

        this.infos = infos;

        logger.info("Playlist loaded.");
    }

    public PlayerPlaylistInfo generateDefaultPlaylistInfo() {
        ArrayList<PlayerVideoInfo> playerVideoInfos = new ArrayList<>(infos);
        Collections.shuffle(playerVideoInfos);
        return new PlayerPlaylistInfo(playerVideoInfos);
    }

    @SneakyThrows
    public PlayerVideoInfo getVideoInfo(String id, boolean def) {
        RequestVideoInfo request = new RequestVideoInfo(id);
        Response<VideoInfo> response = downloader.getVideoInfo(request);
        VideoInfo video = response.data(30, TimeUnit.SECONDS);
        if (video == null) {
            response.error().printStackTrace();
            return null;
        }

        VideoDetails details = video.details();
        return new PlayerVideoInfo(
                def,
                details.title(),
                details.videoId()
        );
    }

    public PlayerVideoInfo getVideoInfo(String id) {
        return this.getVideoInfo(id, false);
    }
}
