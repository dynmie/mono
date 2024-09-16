package me.dynmie.mono.client.queue;

import lombok.Getter;
import me.dynmie.mono.shared.player.PlayerVideoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dynmie
 */
public class Queue {
    private List<ProperVideoInfo> queue = new ArrayList<>();
    private @Getter ProperVideoInfo nowPlaying = null;

    public ProperVideoInfo getNextVideo() {
        if (!queue.isEmpty()) {
            return queue.getFirst();
        }
        return null;
    }

    public void next() {
        if (nowPlaying != null && nowPlaying.getInfo().isDefaultPlaylistVideo()) {
            queue.add(nowPlaying);
        }

        nowPlaying = queue.isEmpty() ? null : queue.removeFirst();
    }

    public void updateQueue(List<PlayerVideoInfo> playerVideoInfos) {
        queue.clear();
        queue = new ArrayList<>(playerVideoInfos.stream().map(ProperVideoInfo::of).toList());
    }

    public List<PlayerVideoInfo> toPlayerVideoInfoList() {
        return queue.stream().map(ProperVideoInfo::getInfo).toList();
    }

}
