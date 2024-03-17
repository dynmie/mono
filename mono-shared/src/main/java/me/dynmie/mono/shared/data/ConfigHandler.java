package me.dynmie.mono.shared.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

/**
 * @author dynmie
 */
@AllArgsConstructor
public class ConfigHandler<T> {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private final File file;

    public void initialize() {
        if (!file.exists()) {
            try {
                saveConfig(getType().getConstructor().newInstance());
            } catch (InstantiationException |
                     IllegalAccessException |
                     InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException("Failed to save config", e);
            }
        }
    }

    public T retrieveConfig() {
        JsonData jsonData = new JsonData(file);
        jsonData.load();
        return gson.fromJson(jsonData.getJsonObject(), getType());
    }

    @SuppressWarnings("unchecked")
    private Class<T> getType() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) type.getActualTypeArguments()[0];
    }

    public void saveConfig(T config) {
        new JsonData(file, gson.toJsonTree(config).getAsJsonObject()).save();
    }

}
