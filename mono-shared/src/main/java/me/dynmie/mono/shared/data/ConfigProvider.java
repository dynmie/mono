package me.dynmie.mono.shared.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A simple utility class for handling configuration data stored in JSON format.
 *
 * @param <T> the type of configuration
 * @author dynmie
 */
@AllArgsConstructor
public class ConfigProvider<T> {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private final Path path;

    /**
     * Initializes the configuration data if the file does not exist.
     * If the file does not exist, a new configuration object of type T will be created and saved.
     */
    private void initialize() {
        if (Files.exists(path)) return;

        try {
            saveConfig(getType().getConstructor().newInstance());
        } catch (InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("Failed to save config", e);
        }
    }

    /**
     * Deserializes the configuration object from the JSON file.
     *
     * @return the configuration object
     */
    public T get() {
        initialize();

        JsonData jsonData = new JsonData(path);
        jsonData.load();
        return gson.fromJson(jsonData.getJsonObject(), getType());
    }

    /**
     * Retrieves the type of the configuration object.
     *
     * @return the class representing the type of configuration
     */
    @SuppressWarnings("unchecked")
    private Class<T> getType() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) type.getActualTypeArguments()[0];
    }

    /**
     * Serializes and saves the configuration object to the JSON file.
     *
     * @param config the configuration object to be saved
     */
    private void saveConfig(T config) {
        new JsonData(path, gson.toJsonTree(config).getAsJsonObject()).save();
    }

}
