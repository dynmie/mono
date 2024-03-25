package me.dynmie.mono.server.command.handler;

import lombok.Getter;
import me.dynmie.mono.server.command.handler.resolver.ArgumentResolver;
import me.dynmie.mono.server.command.handler.resolver.resolvers.BooleanResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dynmie
 */
@Getter
public class CommandHandlerConfiguration {

    private final Map<Class<?>, ArgumentResolver<?>> resolvers = new HashMap<>(Map.of(
            Boolean.class, new BooleanResolver()
    ));

    public <T> void addResolver(Class<T> clazz, ArgumentResolver<T> type) {
        resolvers.put(clazz, type);
    }

}
