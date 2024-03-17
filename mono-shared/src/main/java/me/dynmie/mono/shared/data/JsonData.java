package me.dynmie.mono.shared.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author dynmie
 */
public class JsonData {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private final @Getter File file;
    private @Getter JsonObject jsonObject;

    public JsonData(File file, JsonObject jsonObject) {
        this.file = file;
        this.jsonObject = jsonObject;
    }

    public JsonData(File file) {
        this.file = file;
        this.jsonObject = new JsonObject();
    }

    public void load() {
        if (!file.exists()) {
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createIfNotExists() {
        if (file.exists()) {
            return;
        }
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create Json file", e);
        }
    }

    public void save() {
        createIfNotExists();

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(gson.toJson(jsonObject));
        } catch (Exception e) {
            throw new RuntimeException("Failed to safe Json file", e);
        }
    }

}
