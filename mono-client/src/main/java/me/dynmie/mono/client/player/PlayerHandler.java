package me.dynmie.mono.client.player;

import lombok.Getter;
import org.jline.terminal.Terminal;

import java.io.File;

/**
 * @author dynmie
 */
public class PlayerHandler {

    private @Getter VideoPlayer player;
    private final Terminal terminal;

    public PlayerHandler(Terminal terminal) {
        this.terminal = terminal;
    }

    public void readyFor(File file) {
        stop();
        player = new VideoPlayer(terminal.output(), file, terminal.getWidth(), terminal.getHeight(), true);
    }

    public void stop() {
        if (player != null) {
            player.stop();
        }
    }

}
