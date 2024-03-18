package me.dynmie.mono.server.console;

import lombok.AllArgsConstructor;
import me.dynmie.mono.server.Server;
import me.dynmie.mono.server.command.handler.CommandHandler;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author dynmie
 */
@AllArgsConstructor
public class ServerConsoleHandler {
    private final Server server;
    private final Logger logger;
    private final LineReader lineReader;
    private final CommandHandler commandHandler;

    public void initialize() {
        try {
            while (!Thread.interrupted()) {
                String line = lineReader.readLine("> ");
                if (line == null || line.isEmpty()) {
                    continue;
                }

                String label;
                List<String> args;

                List<String> split = Arrays.asList(line.split(" "));
                if (split.size() > 1) {
                    label = split.getFirst();
                    args = split.subList(1, split.size());
                } else {
                    label = split.getFirst();
                    args = Collections.emptyList();
                }

                try {
                    commandHandler.handleCommand(label, Collections.unmodifiableList(args));
                } catch (Exception e) {
                    for (StackTraceElement element : e.getStackTrace()) {
                        logger.severe(element.toString());
                    }
                }
            }
        } catch (UserInterruptException | EndOfFileException e) {
            server.shutdown();
        }
    }
}
