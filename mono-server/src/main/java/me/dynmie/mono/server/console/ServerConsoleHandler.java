package me.dynmie.mono.server.console;

import lombok.AllArgsConstructor;
import me.dynmie.mono.server.Server;
import me.dynmie.mono.server.command.handler.CommandHandler;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;

/**
 * @author dynmie
 */
@AllArgsConstructor
public class ServerConsoleHandler {
    private final Server server;
    private final LineReader lineReader;
    private final CommandHandler commandHandler;

    public void initialize() {
        try {
            while (!Thread.interrupted()) {
                String line = lineReader.readLine("> ");
                if (line == null || line.isEmpty()) {
                    continue;
                }

                commandHandler.execute(line);
            }
        } catch (UserInterruptException | EndOfFileException e) {
            server.shutdown();
        }
    }
}
