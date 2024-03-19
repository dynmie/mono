package me.dynmie.mono.shared.player;

import lombok.Data;

/**
 * @author dynmie
 */
@Data
public class PlayerInfo {
    private final PlayerVideoInfo nowPlaying;
    private final boolean paused;
}
