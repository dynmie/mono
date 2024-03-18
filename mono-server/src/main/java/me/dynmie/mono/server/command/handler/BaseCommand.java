package me.dynmie.mono.server.command.handler;

import lombok.Getter;
import lombok.Setter;
import me.dynmie.mono.server.command.handler.condition.CommandCondition;

import java.util.*;

/**
 * @author dynmie
 */
public abstract class BaseCommand {

    private final @Getter List<String> aliases;
    private @Setter @Getter String description = "A cool command!";
    private @Setter @Getter String usage = "";
    private @Setter @Getter int minArgs = -1;
    private @Setter @Getter int maxArgs = -1;
    private @Setter @Getter List<CommandCondition> conditions = null;
    private @Setter @Getter String displayName;

    private final @Getter String name;

    private final @Getter Map<String, BaseCommand> subcommandNames = new HashMap<>();
    private final @Getter Map<String, BaseCommand> subcommandAliases = new HashMap<>();

    public BaseCommand(List<String> aliases) {
        this.aliases = aliases;
        name = aliases.getFirst();
        displayName = name;
    }

    public Map<String, BaseCommand> getSubcommands() {
        Map<String, BaseCommand> ret = new HashMap<>(subcommandAliases);
        ret.putAll(subcommandNames);

        return ret;
    }

    public void addSubcommand(BaseCommand command) {
        subcommandNames.put(command.getName(), command);

        for (String alias : command.getAliases()) {
            if (alias.equals(command.getName())) {
                continue;
            }
            subcommandAliases.put(alias, command);
        }
    }

    public void addSubcommands(BaseCommand... commands) {
        for (BaseCommand command : commands) {
            addSubcommand(command);
        }
    }

    public abstract CommandResult onExecute(CommandContext context);
}
