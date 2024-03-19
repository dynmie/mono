package me.dynmie.mono.shared.player;

import lombok.Data;

/**
 * @author dynmie
 */
@Data
public class PlayerVideoInfo {
    private final boolean isDefaultPlaylistVideo;
    private final String title;
    private final String videoId;
}
