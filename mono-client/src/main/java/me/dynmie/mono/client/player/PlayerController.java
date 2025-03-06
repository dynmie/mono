package me.dynmie.mono.client.player;

import lombok.Getter;
import lombok.SneakyThrows;
import me.dynmie.mono.client.utils.ConsoleUtils;
import me.dynmie.mono.shared.player.PlayerConfig;
import org.jline.terminal.Terminal;

import java.nio.file.Path;

/**
 * @author dynmie
 */
public class PlayerController {

    private @Getter VideoPlayer player;
    private final Terminal terminal;

    private Thread controlThread;

    private volatile PlayerConfig config = new PlayerConfig(true, false, true);

    public PlayerController(Terminal terminal) {
        this.terminal = terminal;
    }

    public void initialize() {
        terminal.handle(Terminal.Signal.WINCH, signal -> {
            VideoPlayer plr = getPlayer();
            if (plr == null) return;
            plr.setResolution(terminal.getWidth(), terminal.getHeight());
        });

        controlThread = Thread.startVirtualThread(this::startControls);
    }

    public void shutdown() {
        controlThread.interrupt();
        stop();
    }

    @SneakyThrows
    private void startControls() {
        while (!Thread.interrupted()) {
            int read = terminal.reader().read();
            char character = (char) read;

            if (player == null) {
                continue;
            }

            switch (character) {
                case 'c' -> {
                    Asciifier old = player.getAsciifier();
                    player.setAsciifier(new Asciifier(
                            !old.isColor(),
                            old.isFullPixel(),
                            old.isTextDithering(),
                            brightnessLevels(old.isColor())
                    ));
                }
                case 'd' -> {
                    Asciifier old = player.getAsciifier();
                    player.setAsciifier(new Asciifier(
                            old.isColor(),
                            old.isFullPixel(),
                            !old.isTextDithering(),
                            brightnessLevels(old.isColor())
                    ));
                }
                case 'f' -> {
                    Asciifier old = player.getAsciifier();
                    player.setAsciifier(new Asciifier(
                            old.isColor(),
                            !old.isFullPixel(),
                            old.isTextDithering(),
                            brightnessLevels(old.isColor())
                    ));
                }
                case ' ' -> {
                    if (player.isPaused()) {
                        player.play();
                    } else {
                        player.pause();
                    }
                }
                case 'n' -> player.stop();
            }
        }
    }

    public void readyFor(Path path) {
        stop();
        player = new VideoPlayer(System.out, path.toFile(), terminal.getWidth(), terminal.getHeight(), new Asciifier(
                config.isColor(),
                config.isFullPixel(),
                config.isTextDithering(),
                brightnessLevels(config.isColor())
        ));
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

    public void pause() {
        if (player != null) {
            player.pause();
        }
    }

    private char[] brightnessLevels(boolean color) {
        if (color) return Asciifier.DEFAULT_COLOR_BRIGHTNESS_LEVELS;
        return Asciifier.DEFAULT_BRIGHTNESS_LEVELS;
    }

    public void setConfig(PlayerConfig config) {
        PlayerConfig playerConfig = this.config;
        this.config = config;
        if (player != null) {
            player.setAsciifier(
                    new Asciifier(
                            config.isColor(),
                            config.isFullPixel(),
                            config.isTextDithering(),
                            brightnessLevels(config.isColor())
                    )
            );

            // fix color not being white when changing colors
            // i'm sorry, i was lazy. i know this isn't a good solution.
            Thread.startVirtualThread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (!this.config.isColor() && playerConfig.isColor()) {
                    System.out.print(ConsoleUtils.getForegroundResetCode());
                }
                if (!this.config.isFullPixel() && playerConfig.isFullPixel()) {
                    System.out.print(ConsoleUtils.getBackgroundResetCode());
                }
            });
        }
    }

}
