package me.dynmie.mono.shared.player;

import lombok.Data;

import java.util.List;

/**
 * @author dynmie
 */
@Data
public class PlayerPlaylistInfo {
    private final List<PlayerVideoInfo> videos;
}
