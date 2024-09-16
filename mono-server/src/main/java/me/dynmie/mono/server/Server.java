package me.dynmie.mono.server;

import lombok.Getter;
import me.dynmie.jeorge.Injector;
import me.dynmie.jeorge.Jeorge;
import me.dynmie.mono.server.client.ClientService;
import me.dynmie.mono.server.client.session.SessionService;
import me.dynmie.mono.server.command.handler.CommandHandler;
import me.dynmie.mono.server.command.server.CommandInitializer;
import me.dynmie.mono.server.command.server.ServerCommandHandlerConfiguration;
import me.dynmie.mono.server.console.ConsoleHandler;
import me.dynmie.mono.server.data.ServerConfig;
import me.dynmie.mono.server.data.ServerConfigProvider;
import me.dynmie.mono.server.jeorge.ServerBinder;
import me.dynmie.mono.server.network.connection.ConnectionService;
import me.dynmie.mono.server.network.netty.NetworkHandler;
import me.dynmie.mono.server.player.VideoService;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author dynmie
 */
public class Server {

    private final @Getter Logger logger = Logger.getLogger("Server");
    private final @Getter Path workingFolderPath = Paths.get("mono-data", "server");
    private final @Getter Terminal terminal;
    private final @Getter LineReader lineReader;

    private NetworkHandler networkHandler;

    private @Getter Injector injector;

    {
        // Logger setup
        logger.setUseParentHandlers(false);
        java.util.logging.ConsoleHandler handler = new java.util.logging.ConsoleHandler();
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
            lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Starts the server.
     */
    public void start() {
        logger.info("Starting server...");

        ServerConfigProvider configHandler = new ServerConfigProvider(workingFolderPath.resolve("config.json"));
        ServerConfig config = configHandler.get();

        SessionService sessionService = new SessionService();
        ConnectionService connectionService = new ConnectionService(this);
        ClientService clientService = new ClientService();

        networkHandler = new NetworkHandler(logger, config.getNetworkInformation(), connectionService, sessionService, clientService);

        VideoService videoService = new VideoService(config.getPlayerConfiguration(), logger);
        videoService.initialize();

        ServerCommandHandlerConfiguration commandHandlerConfig = new ServerCommandHandlerConfiguration(
                clientService
        );
        CommandHandler commandHandler = new CommandHandler(commandHandlerConfig, logger);

        injector = Jeorge.createInjector(new ServerBinder(
                this,
                logger,
                sessionService,
                connectionService,
                clientService,
                networkHandler,
                videoService,
                commandHandler,
                config
        ));

        CommandInitializer commandInitializer = new CommandInitializer(injector, commandHandler);
        commandInitializer.initialize();

        ConsoleHandler consoleHandler = new ConsoleHandler(this, lineReader, commandHandler);

        networkHandler.start();

        logger.info("Welcome to Mono!");
        consoleHandler.initialize();
    }

    /**
     * Initiates the shutdown sequence of the server.
     */
    public void shutdown() {
        logger.info("Shutting down...");
        networkHandler.shutdown();
        logger.info("Goodbye!");
        System.exit(0);
    }

}
