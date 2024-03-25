package me.dynmie.mono.server.command.handler;

import lombok.Getter;
import me.dynmie.mono.server.command.handler.resolver.ArgumentResolver;

import java.util.List;
import java.util.StringJoiner;

/**
 * @author dynmie
 */
public class CommandContext {

    private final @Getter BaseCommand command;
    private final @Getter String path;
    private final @Getter CommandHandler handler;
    private final @Getter String label;
    private final @Getter List<String> args;

    public CommandContext(BaseCommand command, String path, CommandHandler handler, String label, List<String> args) {
        this.command = command;
        this.path = path;
        this.handler = handler;
        this.label = label;
        this.args = args;
    }

    public void sendMessage(String message) {
        if (message.contains("\n")) {
            String[] split = message.split("\n");
            for (String s : split) {
                handler.getLogger().info(s);
            }
            return;
        }
        handler.getLogger().info(message);
    }

    public int size() {
        return args.size();
    }

    public String getArgAt(int i) {
        if (!isArgAt(i)) {
            return null;
        }

        return args.get(i);
    }

    public boolean isArgAt(int i) {
        return !(i >= args.size());
    }

    public String getStringAll() {
        StringJoiner joiner = new StringJoiner(" ");

        for (String arg : args) {
            joiner.add(arg);
        }

        return joiner.toString();
    }

    public String getStringAt(int pos) {
        if (!isArgAt(pos)) {
            return null;
        }

        StringJoiner joiner = new StringJoiner(" ");

        for (int i = 0; i < args.size(); i++) {
            if (pos > i) continue;

            String s = args.get(i);
            joiner.add(s);
        }

        return joiner.toString();
    }

    public Integer getIntegerAt(int pos) {
        if (!isArgAt(pos)) {
            return null;
        }

        return Integer.parseInt(getArgAt(pos));
    }

    public boolean isIntegerAt(int pos) {
        return getIntegerAt(pos) != null;
    }

    public <T> T getAt(int pos, Class<T> clazz) {
        if (!isArgAt(pos)) {
            return null;
        }

        @SuppressWarnings("unchecked") // checked
        ArgumentResolver<T> resolver = (ArgumentResolver<T>) handler.getConfiguration().getResolvers().get(clazz);
        if (resolver == null) {
            return null;
        }

        return resolver.resolve(this, getArgAt(pos));
    }

    public <T> boolean isAt(int pos, Class<T> clazz) {
        return getAt(pos, clazz) != null;
    }

}
