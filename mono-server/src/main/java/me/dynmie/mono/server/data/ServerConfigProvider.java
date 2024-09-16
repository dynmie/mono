package me.dynmie.mono.server.data;

import me.dynmie.mono.shared.data.ConfigProvider;

import java.nio.file.Path;

/**
 * @author dynmie
 */
public class ServerConfigProvider extends ConfigProvider<ServerConfig> {
    public ServerConfigProvider(Path path) {
        super(path);
    }
}
