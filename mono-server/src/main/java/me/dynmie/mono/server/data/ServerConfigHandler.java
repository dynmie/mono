package me.dynmie.mono.server.data;

import me.dynmie.mono.shared.data.ConfigHandler;

import java.io.File;

/**
 * @author dynmie
 */
public class ServerConfigHandler extends ConfigHandler<ServerConfig> {
    public ServerConfigHandler(File file) {
        super(file);
    }
}
