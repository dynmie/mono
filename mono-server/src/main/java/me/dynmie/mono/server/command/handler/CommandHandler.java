package me.dynmie.mono.server.command.handler;

import lombok.AccessLevel;
import lombok.Getter;
import me.dynmie.mono.server.command.handler.condition.CommandCondition;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * @author dynmie
 */
public class CommandHandler {

    private final Map<String, BaseCommand> registeredAliases = new ConcurrentHashMap<>();
    private final Map<String, BaseCommand> registeredCommands = new ConcurrentHashMap<>();

    private final @Getter CommandHandlerConfiguration configuration;
    private final @Getter(AccessLevel.PACKAGE) Logger logger;

    public CommandHandler(CommandHandlerConfiguration configuration, Logger logger) {
        this.configuration = configuration;
        this.logger = logger;
    }

    public Map<String, BaseCommand> getCommands() {
        return Collections.unmodifiableMap(registeredCommands);
    }

    public void register(BaseCommand command) {
        for (String alias : command.getAliases()) {
            registeredAliases.put(alias.toLowerCase(), command);
        }
        registeredCommands.put(command.getName().toLowerCase(), command);
    }

    public void handleCommand(String label, List<String> args) {
        BaseCommand baseCommand = registeredAliases.get(label.toLowerCase());
        if (baseCommand == null) {
            logger.info("Unknown command. Type \"help\" for help.");
            return;
        }

        StringJoiner path = new StringJoiner(" ");
        path.add(baseCommand.getName());

        BaseCommand previous;
        BaseCommand current = baseCommand;
        int pos = 0;
        boolean subcommand = false;
        for (String arg : args) {
            previous = current;

            current = previous.getSubcommands().get(arg);

            if (current == null) {
                current = previous;
                break;
            }

            path.add(current.getName());
            subcommand = true;
            pos++;
        }

        String usage = path + " " + current.getUsage();
        String pathStr = path.toString();

        List<String> argsToReturn = new ArrayList<>(args);
        if (subcommand) {
            argsToReturn = new ArrayList<>(argsToReturn.subList(pos, args.size()));
        }

        CommandContext context = new CommandContext(
                current,
                pathStr,
                this,
                label,
                argsToReturn
        );


        // MIN MAX ARGS
        if (current.getMinArgs() != -1 && argsToReturn.size() < current.getMinArgs()) {
            logger.info("Usage: /" + usage);
            return;
        }

        if (current.getMaxArgs() != -1 && argsToReturn.size() > current.getMaxArgs()) {
            logger.info("Usage: /" + usage);
            return;
        }

        // CONDITION
        if (current.getConditions() != null) {
            for (int i = 0; i < argsToReturn.size(); i++) {
                if (current.getConditions().isEmpty()) continue;
                if (i >= current.getConditions().size()) continue;

                CommandCondition condition = current.getConditions().get(i);
                if (condition == null) continue;

                CommandResult status = condition.check(argsToReturn.get(i));
                runStat(status, usage);

                if (!status.isSuccess()) return;
            }
        }


        // EXECUTION
        execute(current, context, usage);

    }

    private void execute(BaseCommand command, CommandContext context, String usage) {
        CommandResult status;
        try {
            status = command.onExecute(context);
        } catch (Throwable throwable) {
            throw new RuntimeException("Unhandled exception while executing command '" + command.getName() + "'", throwable);
        }

        runStat(status, usage);
    }

    private void runStat(CommandResult status, String usage) {
        if (status == CommandResult.INCORRECT_USAGE) {
            logger.info("Usage: /" + usage);
        }
    }

}
