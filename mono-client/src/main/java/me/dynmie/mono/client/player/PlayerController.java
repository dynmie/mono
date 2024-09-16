package me.dynmie.mono.client.player;

import lombok.Getter;
import me.dynmie.mono.client.network.NetworkHandler;
import me.dynmie.mono.client.queue.ProperVideoInfo;
import me.dynmie.mono.client.queue.QueueService;
import me.dynmie.mono.client.utils.ConsoleUtils;
import me.dynmie.mono.shared.packet.ready.server.ServerboundPlayerInfoPacket;
import me.dynmie.mono.shared.player.PlayerConfig;
import me.dynmie.mono.shared.player.PlayerInfo;
import org.jline.terminal.Terminal;

import java.io.File;

/**
 * @author dynmie
 */
public class PlayerController {

    private @Getter VideoPlayer player;
    private final Terminal terminal;
    private final QueueService queueService;
    private final NetworkHandler networkHandler;

    private Thread thread;
    public static final Object LOCK = new Object();

    private volatile PlayerConfig config = new PlayerConfig(true, false, true);

    public PlayerController(Terminal terminal, QueueService queueService, NetworkHandler networkHandler) {
        this.terminal = terminal;
        this.queueService = queueService;
        this.networkHandler = networkHandler;
    }

    public void initialize() {
        terminal.handle(Terminal.Signal.WINCH, signal -> {
            VideoPlayer plr = getPlayer();
            if (plr == null) return;
            plr.setResolution(terminal.getWidth(), terminal.getHeight());
        });

        thread = Thread.startVirtualThread(() -> {
            while (!thread.isInterrupted()) {
                queueService.onVideoPrePlay();

                ProperVideoInfo nowPlaying = queueService.getQueue().getNowPlaying();
                if (nowPlaying == null || !nowPlaying.isSuccess()) {
                    synchronized (LOCK) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    continue;
                }

                queueService.onVideoPlay();
                readyFor(nowPlaying.getFile());
                play();

                networkHandler.getConnection().sendPacket(new ServerboundPlayerInfoPacket(
                        new PlayerInfo(nowPlaying.getInfo(), player.isPaused())
                ));

                try {
                    player.awaitFinish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                queueService.onVideoEnd();
            }
        });
    }

    private void readyFor(File file) {
        stop();
        player = new VideoPlayer(System.out, file, terminal.getWidth(), terminal.getHeight(), new Asciifier(
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
