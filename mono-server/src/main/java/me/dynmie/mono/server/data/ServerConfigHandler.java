package me.dynmie.mono.server.data;

import me.dynmie.mono.shared.data.ConfigHandler;

import java.nio.file.Path;

/**
 * @author dynmie
 */
public class ServerConfigHandler extends ConfigHandler<ServerConfig> {
    public ServerConfigHandler(Path path) {
        super(path);
    }
}
