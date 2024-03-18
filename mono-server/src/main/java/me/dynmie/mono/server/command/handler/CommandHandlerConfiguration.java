package me.dynmie.mono.server.command.handler;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author dynmie
 */
@Getter
public class CommandHandlerConfiguration {

    private final Map<Class<?>, Function<String, ?>> resolvers = new HashMap<>();

    public <T> void addResolver(Class<T> clazz, Function<String, T> resolver) {
        resolvers.put(clazz, resolver);
    }

}
