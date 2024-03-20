package me.dynmie.mono.client.player;

import lombok.Getter;
import me.dynmie.mono.client.network.NetworkHandler;
import me.dynmie.mono.client.utils.ConsoleUtils;
import me.dynmie.mono.client.utils.FrameUtils;
import me.dynmie.mono.shared.packet.ready.server.ServerboundPlayerInfoPacket;
import me.dynmie.mono.shared.player.PlayerConfig;
import me.dynmie.mono.shared.player.PlayerInfo;
import org.jline.terminal.Terminal;

import java.io.File;

/**
 * @author dynmie
 */
public class PlayerHandler {

    private @Getter VideoPlayer player;
    private final Terminal terminal;
    private final QueueHandler queueHandler;
    private final NetworkHandler networkHandler;

    private Thread thread;
    public static final Object LOCK = new Object();

    private volatile PlayerConfig config = new PlayerConfig(true, false);

    public PlayerHandler(Terminal terminal, QueueHandler queueHandler, NetworkHandler networkHandler) {
        this.terminal = terminal;
        this.queueHandler = queueHandler;
        this.networkHandler = networkHandler;
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

        thread = Thread.startVirtualThread(() -> {
            while (!thread.isInterrupted()) {
                ActualVideoInfo nextVideo = queueHandler.getNextVideo();
                if (nextVideo == null) {
                    synchronized (LOCK) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                nextVideo = queueHandler.getNextVideo();
                queueHandler.setNextVideo(null);
                queueHandler.setNowPlaying(nextVideo);
                readyFor(nextVideo.file());
                play();

                networkHandler.getConnection().sendPacket(new ServerboundPlayerInfoPacket(
                        new PlayerInfo(nextVideo.info(), player.isPaused())
                ));

                queueHandler.next();
                queueHandler.update(false);
                try {
                    player.awaitFinish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void readyFor(File file) {
        stop();
        player = new VideoPlayer(terminal.output(), file, terminal.getWidth(), terminal.getHeight(), config);
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

    public void setConfig(PlayerConfig config) {
        boolean color = this.config.isColor();
        this.config = config;
        if (player != null) {
            // fix color not being white when changing colors
            if (color && !config.isColor()) {
                ConsoleUtils.resetCursorPosition();
                terminal.writer().println(FrameUtils.getColorEscapeCode(255, 255, 255));
            }
            player.setConfig(config);
        }
    }

}
