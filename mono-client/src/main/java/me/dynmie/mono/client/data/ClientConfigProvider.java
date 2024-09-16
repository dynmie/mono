package me.dynmie.mono.client.data;

import me.dynmie.mono.shared.data.ConfigProvider;

import java.nio.file.Path;

/**
 * @author dynmie
 */
public class ClientConfigProvider extends ConfigProvider<ClientConfig> {
    public ClientConfigProvider(Path path) {
        super(path);
    }
}
