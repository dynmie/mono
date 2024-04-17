package me.dynmie.mono.shared.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Cleanup;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A utility class for handling JSON data stored in files.
 *
 * @author dynmie
 */
@Getter
public class JsonData {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private final Path path;
    private JsonObject jsonObject;

    /**
     * Constructs a JsonData object with the specified file path and JSON object.
     *
     * @param path the path to the JSON file
     * @param jsonObject the JSON object containing the data
     */
    public JsonData(Path path, JsonObject jsonObject) {
        this.path = path;
        this.jsonObject = jsonObject;
    }

    /**
     * Constructs a JsonData object with the specified file path and initializes an empty JSON object.
     *
     * @param path the path to the JSON file
     */
    public JsonData(Path path) {
        this.path = path;
        this.jsonObject = new JsonObject();
    }

    /**
     * Loads JSON data from the file into the JSON object.
     */
    @SneakyThrows
    public void load() {
        if (!Files.exists(path)) {
            return;
        }

        @Cleanup BufferedReader reader = Files.newBufferedReader(path);

        jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
    }

    /**
     * Creates the file and its parent directories if they do not exist.
     */
    @SneakyThrows
    public void createIfNotExists() {
        if (Files.exists(path)) {
            return;
        }

        Files.createDirectories(path.getParent());
        Files.createFile(path);
    }

    /**
     * Saves the JSON data to the file.
     */
    @SneakyThrows
    public void save() {
        createIfNotExists();
        Files.writeString(path, gson.toJson(jsonObject));
    }

}
