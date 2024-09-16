package me.dynmie.mono.client.jeorge;

import lombok.AllArgsConstructor;
import me.dynmie.jeorge.Binder;
import me.dynmie.mono.client.QClient;
import me.dynmie.mono.client.data.ClientConfig;
import me.dynmie.mono.client.data.ClientConfigProvider;
import me.dynmie.mono.client.network.NetworkHandler;
import me.dynmie.mono.client.player.PlayerController;
import me.dynmie.mono.client.queue.QueueService;

import java.util.logging.Logger;

/**
 * @author dynmie
 */
@AllArgsConstructor
public class ClientBinder extends Binder {
    private final QClient client;
    private final NetworkHandler networkHandler;
    private final ClientConfigProvider configHandler;
    private final PlayerController playerController;
    private final QueueService queueService;
    private final ClientConfig config;

    @Override
    public void configure() {
        bind(QClient.class, client);
        bind(Logger.class, client.getLogger());
        bind(NetworkHandler.class, networkHandler);
        bind(ClientConfigProvider.class, configHandler);
        bind(PlayerController.class, playerController);
        bind(QueueService.class, queueService);
        bind(ClientConfig.class, config);
    }
}
