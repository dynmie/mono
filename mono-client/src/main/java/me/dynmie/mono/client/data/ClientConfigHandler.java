package me.dynmie.mono.client.data;

import me.dynmie.mono.shared.data.ConfigHandler;

import java.io.File;

/**
 * @author dynmie
 */
public class ClientConfigHandler extends ConfigHandler<ClientConfig> {
    public ClientConfigHandler(File file) {
        super(file);
    }
}
