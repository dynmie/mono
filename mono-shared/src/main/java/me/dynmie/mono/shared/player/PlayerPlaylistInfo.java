package me.dynmie.mono.shared.player;

import lombok.Value;

import java.util.List;

/**
 * @author dynmie
 */
@Value
public class PlayerPlaylistInfo {
    List<PlayerVideoInfo> videos;
}
