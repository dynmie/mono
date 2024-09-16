package me.dynmie.mono.shared.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dynmie
 */
public class EventDispatcher {
    private final Map<Class<? extends Event>, Set<Listener>> listeners = new HashMap<>();

    private Set<Listener> getListenersByEventType(Class<? extends Event> type) {
        return listeners.computeIfAbsent(type, t -> new HashSet<>());
    }

    private Set<Method> getHandlersInListener(Listener listener) {
        Set<Method> handlers = new HashSet<>();

        for (Method method : listener.getClass().getMethods()) {
            if (!method.isAnnotationPresent(EventHandler.class)) continue;
            if (method.getParameterCount() == 0) continue;

            Class<?> parameterType = method.getParameterTypes()[0];
            if (!Event.class.isAssignableFrom(parameterType)) {
                throw new NotAnEventException("Parameter %s is not an event.".formatted(parameterType.getName()));
            }

            handlers.add(method);
        }

        return handlers;
    }

    private Set<Method> getHandlersInListenerOfType(Listener listener, Class<? extends Event> type) {
        return getHandlersInListener(listener).stream()
                .filter(m -> type == m.getParameterTypes()[0])
                .collect(Collectors.toUnmodifiableSet());
    }

    public void registerListener(Listener listener) {
        Set<Method> handlers = getHandlersInListener(listener);
        if (handlers.isEmpty()) return;

        handlers.stream().map(method -> method.getParameterTypes()[0]).forEach(type -> {
            // already checked
            //noinspection unchecked
            getListenersByEventType((Class<? extends Event>) type).add(listener);
        });
    }

    public void callEvent(Event event) {
        Class<? extends Event> type = event.getClass();

        getListenersByEventType(type).stream()
                .flatMap(l -> getHandlersInListener(l).stream()
                        .filter(m -> type == m.getParameterTypes()[0])
                        .map(m -> Map.entry(l, m)))
                .forEach(e -> {
                    Method method = e.getValue();
                    Listener instance = e.getKey();

                    try {
                        method.invoke(instance, event);
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        throw new EventDispatchFailedException("Failed to invoke method %s for event %s in listener %s"
                                .formatted(method.getName(), type.getName(), instance.getClass().getName()), ex);
                    }
                });
    }

}
