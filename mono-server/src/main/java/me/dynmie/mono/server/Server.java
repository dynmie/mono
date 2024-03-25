package me.dynmie.mono.server;

import lombok.Getter;
import me.dynmie.jeorge.Injector;
import me.dynmie.jeorge.Jeorge;
import me.dynmie.mono.server.client.ClientHandler;
import me.dynmie.mono.server.client.session.SessionHandler;
import me.dynmie.mono.server.command.handler.CommandHandler;
import me.dynmie.mono.server.command.server.CommandRegistrationHandler;
import me.dynmie.mono.server.command.server.ServerCommandHandlerConfiguration;
import me.dynmie.mono.server.console.ServerConsoleHandler;
import me.dynmie.mono.server.data.ServerConfig;
import me.dynmie.mono.server.data.ServerConfigHandler;
import me.dynmie.mono.server.jeorge.ServerBinder;
import me.dynmie.mono.server.network.connection.ConnectionHandler;
import me.dynmie.mono.server.network.netty.NetworkHandler;
import me.dynmie.mono.server.player.VideoHandler;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

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
public class Server {

    private final @Getter Logger logger = Logger.getLogger("Server");
    private final @Getter File workingFolder = new File("mono-data", "server");
    private final @Getter Terminal terminal;
    private final @Getter LineReader lineReader;

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
            lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        logger.info("Starting server...");

        ServerConfigHandler configHandler = new ServerConfigHandler(new File(workingFolder, "config.json"));
        configHandler.initialize();
        ServerConfig config = configHandler.retrieveConfig();

        SessionHandler sessionHandler = new SessionHandler();
        ConnectionHandler connectionHandler = new ConnectionHandler(this);
        ClientHandler clientHandler = new ClientHandler();

        networkHandler = new NetworkHandler(logger, config.getNetworkInformation(), connectionHandler, sessionHandler, clientHandler);

        VideoHandler videoHandler = new VideoHandler(config.getPlayerConfiguration(), logger);
        videoHandler.initialize();

        ServerCommandHandlerConfiguration commandHandlerConfig = new ServerCommandHandlerConfiguration(
                clientHandler
        );
        CommandHandler commandHandler = new CommandHandler(commandHandlerConfig, logger);

        injector = Jeorge.createInjector(new ServerBinder(
                this,
                logger,
                sessionHandler,
                connectionHandler,
                clientHandler,
                networkHandler,
                videoHandler,
                commandHandler,
                config
        ));

        CommandRegistrationHandler commandRegistrationHandler = new CommandRegistrationHandler(injector, commandHandler);
        commandRegistrationHandler.initialize();

        ServerConsoleHandler serverConsoleHandler = new ServerConsoleHandler(this, lineReader, commandHandler);

        networkHandler.start();

        logger.info("Welcome to Mono!");
        serverConsoleHandler.initialize();
    }

    public void shutdown() {
        logger.info("Shutting down...");
        networkHandler.shutdown();
        logger.info("Goodbye!");
        System.exit(0);
    }

}
