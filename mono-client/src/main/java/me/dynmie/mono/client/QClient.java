package me.dynmie.mono.client;

import lombok.Getter;
import me.dynmie.jeorge.Injector;
import me.dynmie.jeorge.Jeorge;
import me.dynmie.mono.client.data.ClientConfig;
import me.dynmie.mono.client.data.ClientConfigProvider;
import me.dynmie.mono.client.jeorge.ClientBinder;
import me.dynmie.mono.client.network.NetworkHandler;
import me.dynmie.mono.client.player.PlayerController;
import me.dynmie.mono.client.queue.QueueHandler;
import me.dynmie.mono.client.queue.QueueService;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author dynmie
 */
public class QClient {

    private final @Getter Logger logger = Logger.getLogger("Client");
    private final @Getter Path workingFolderPath = Paths.get("mono-data", "client");
    private final @Getter Terminal terminal;

    private NetworkHandler networkHandler;
    private PlayerController playerController;
    private QueueHandler queueHandler;

    private @Getter Injector injector;

    {
        // Logger setup
        logger.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            @Override
            public synchronized String format(LogRecord record) {
                return String.format("[%1$tF %1$tT] [%2$s] %3$s %n",
                        new Date(record.getMillis()),
                        record.getLevel().getLocalizedName(),
                        record.getMessage());
            }
        });
        logger.addHandler(handler);

        // Terminal setup
        try {
            terminal = TerminalBuilder.builder().dumb(true).build();
            terminal.puts(InfoCmp.Capability.cursor_invisible);
            terminal.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (Terminal.TYPE_DUMB.equals(terminal.getType()) || Terminal.TYPE_DUMB_COLOR.equals(terminal.getType())) {
            logger.warning("You are using a dumb terminal! Frames may not render properly.");
        }
    }

    /**
     * Starts the client.
     */
    public void start() {
        ClientConfigProvider configHandler = new ClientConfigProvider(workingFolderPath.resolve("config.json"));
        ClientConfig config = configHandler.get();

        networkHandler = new NetworkHandler(this, logger, config.getNetworkInformation());

        playerController = new PlayerController(terminal);
        playerController.initialize();

        QueueService queueService = new QueueService(getWorkingFolderPath());
        queueHandler = new QueueHandler(networkHandler, queueService, playerController);
        queueHandler.initialize();

        injector = Jeorge.createInjector(new ClientBinder(
                this,
                networkHandler,
                configHandler,
                playerController,
                queueService,
                config
        ));

        networkHandler.start();
        playerController.initialize();
    }

    /**
     * Initiates the shutdown sequence of the client.
     */
    public void shutdown() {
        logger.info("Shutting down...");

        networkHandler.shutdown();
        playerController.shutdown();
        queueHandler.shutdown();

        logger.info("Goodbye!");
        System.exit(0);
    }

}
