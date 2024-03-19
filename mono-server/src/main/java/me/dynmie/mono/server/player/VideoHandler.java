package me.dynmie.mono.server.player;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestPlaylistInfo;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.playlist.PlaylistInfo;
import com.github.kiulian.downloader.model.playlist.PlaylistVideoDetails;
import com.github.kiulian.downloader.model.videos.VideoDetails;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.dynmie.mono.server.data.ServerConfig;
import me.dynmie.mono.shared.player.PlayerPlaylistInfo;
import me.dynmie.mono.shared.player.PlayerVideoInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author dynmie
 */
@RequiredArgsConstructor
public class VideoHandler {

    private final ServerConfig.PlayerConfiguration configuration;
    private final Logger logger;
    private final YoutubeDownloader downloader = new YoutubeDownloader();
    private @Getter PlayerPlaylistInfo defaultPlaylistInfo;

    public void initialize() {
        String playlistId = configuration.getDefaultPlaylistId();
        logger.info("Loading playlist '" + playlistId + "'...");
        RequestPlaylistInfo request = new RequestPlaylistInfo(playlistId);
        Response<PlaylistInfo> response = downloader.getPlaylistInfo(request);
        PlaylistInfo playlistInfo = response.data();

        List<PlayerVideoInfo> infos = new ArrayList<>();
        for (PlaylistVideoDetails video : playlistInfo.videos()) {
            infos.add(
                    new PlayerVideoInfo(true, video.title(), video.videoId())
            );
        }
        Collections.shuffle(infos);

        defaultPlaylistInfo = new PlayerPlaylistInfo(infos);
        logger.info("Playlist loaded.");
    }

    public PlayerVideoInfo getVideoInfo(String id, boolean def) {
        RequestVideoInfo request = new RequestVideoInfo(id);
        Response<VideoInfo> response = downloader.getVideoInfo(request);
        VideoInfo video = response.data();
        if (video == null) return null;

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
