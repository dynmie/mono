package me.dynmie.mono.shared.player;

import lombok.Value;

/**
 * @author dynmie
 */
@Value
public class PlayerVideoInfo {
    boolean isDefaultPlaylistVideo;
    String title;
    String videoId;

    public String getDisplayName() {
        return title + " (" + videoId + ")";
    }
}
