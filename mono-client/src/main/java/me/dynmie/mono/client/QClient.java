package me.dynmie.mono.client;

import lombok.Getter;
import me.dynmie.jeorge.Injector;
import me.dynmie.jeorge.Jeorge;
import me.dynmie.mono.client.data.ClientConfig;
import me.dynmie.mono.client.data.ClientConfigHandler;
import me.dynmie.mono.client.jeorge.ClientBinder;
import me.dynmie.mono.client.network.NetworkHandler;
import me.dynmie.mono.client.player.PlayerHandler;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.File;
import java.io.IOException;
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
    private final @Getter File workingFolder = new File("mono-data", "client");
    private final @Getter Terminal terminal;

    private NetworkHandler networkHandler;

    private @Getter Injector injector;

    {
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

    public void start() {
        ClientConfigHandler configHandler = new ClientConfigHandler(new File(workingFolder, "config.json"));
        configHandler.initialize();
        ClientConfig config = configHandler.retrieveConfig();

        networkHandler = new NetworkHandler(this, logger, config.getNetworkInformation());

        PlayerHandler playerHandler = new PlayerHandler(terminal);
        playerHandler.initialize();

        injector = Jeorge.createInjector(new ClientBinder(
                this,
                networkHandler,
                configHandler,
                playerHandler,
                config
        ));

        networkHandler.start();
    }

    public void shutdown() {
        logger.info("Shutting down...");
        networkHandler.shutdown();
        logger.info("Goodbye!");
        System.exit(0);
    }

}
