package me.dynmie.mono.client.data;

import me.dynmie.mono.shared.data.ConfigHandler;

import java.nio.file.Path;

/**
 * @author dynmie
 */
public class ClientConfigHandler extends ConfigHandler<ClientConfig> {
    public ClientConfigHandler(Path path) {
        super(path);
    }
}
