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

    public void initialize() {
        Terminal.SignalHandler signalHandler = signal -> {
            if (signal == Terminal.Signal.WINCH) {
                VideoPlayer plr = getPlayer();
                if (plr == null) return;
                plr.setResolution(terminal.getWidth(), terminal.getHeight());
            }
        };

        terminal.handle(Terminal.Signal.WINCH, signalHandler);
    }

    public void readyFor(File file) {
        stop();
        player = new VideoPlayer(terminal.output(), file, terminal.getWidth(), terminal.getHeight(), true);
        player.start();
    }

    public void play() {
        if (player != null) {
            player.play();
        }
    }

    public void stop() {
        if (player != null) {
            player.stop();
        }
    }

}
