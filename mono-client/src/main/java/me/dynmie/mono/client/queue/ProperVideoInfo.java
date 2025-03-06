package me.dynmie.mono.client.queue;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.dynmie.mono.shared.player.PlayerVideoInfo;

import java.nio.file.Path;

/**
 * @author dynmie
 */
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProperVideoInfo {
    private Path path;
    private boolean taskTaken;
    private boolean finished;
    private @Getter(AccessLevel.NONE) boolean success;
    private final PlayerVideoInfo info;

    public boolean isSuccess() {
        return finished && success;
    }

    public boolean isFailed() {
        return finished && !success;
    }

    public static ProperVideoInfo of(PlayerVideoInfo info) {
        return new ProperVideoInfo(null, false, false, false, info);
    }
}
