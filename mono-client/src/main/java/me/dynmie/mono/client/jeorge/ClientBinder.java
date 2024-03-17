package me.dynmie.mono.client.jeorge;

import lombok.AllArgsConstructor;
import me.dynmie.jeorge.Binder;
import me.dynmie.mono.client.QClient;
import me.dynmie.mono.client.data.ClientConfig;
import me.dynmie.mono.client.data.ClientConfigHandler;
import me.dynmie.mono.client.network.NetworkHandler;
import me.dynmie.mono.client.player.PlayerHandler;

import java.util.logging.Logger;

/**
 * @author dynmie
 */
@AllArgsConstructor
public class ClientBinder extends Binder {
    private final QClient client;
    private final NetworkHandler networkHandler;
    private final ClientConfigHandler configHandler;
    private final PlayerHandler playerHandler;
    private final ClientConfig config;

    @Override
    public void configure() {
        bind(QClient.class, client);
        bind(Logger.class, client.getLogger());
        bind(NetworkHandler.class, networkHandler);
        bind(ClientConfigHandler.class, configHandler);
        bind(PlayerHandler.class, playerHandler);
        bind(ClientConfig.class, config);
    }
}
