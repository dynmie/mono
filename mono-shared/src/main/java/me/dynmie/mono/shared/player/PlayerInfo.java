package me.dynmie.mono.shared.player;

import lombok.Value;

/**
 * @author dynmie
 */
@Value
public class PlayerInfo {
    PlayerVideoInfo nowPlaying;
    boolean paused;
}
