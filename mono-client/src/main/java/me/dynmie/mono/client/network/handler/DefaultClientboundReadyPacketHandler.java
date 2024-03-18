package me.dynmie.mono.client.network.handler;

import me.dynmie.jeorge.Inject;
import me.dynmie.jeorge.Injector;
import me.dynmie.mono.client.QClient;
import me.dynmie.mono.client.network.connection.ServerConnection;
import me.dynmie.mono.client.player.PlayerHandler;
import me.dynmie.mono.shared.packet.ready.ClientboundReadyPacketHandler;
import me.dynmie.mono.shared.packet.ready.client.ClientboundPlayerPausePacket;
import me.dynmie.mono.shared.packet.ready.client.ClientboundPlayerPlayPacket;

import java.io.File;

/**
 * @author dynmie
 */
public class DefaultClientboundReadyPacketHandler implements ClientboundReadyPacketHandler {

    private final Injector injector;
    private final ServerConnection serverConnection;

    @Inject
    private QClient client;

    @Inject
    private PlayerHandler playerHandler;

    public DefaultClientboundReadyPacketHandler(Injector injector, ServerConnection serverConnection) {
        this.injector = injector;
        this.serverConnection = serverConnection;

        injector.injectMembers(this);
    }

    @Override
    public void onPlayerPlay(ClientboundPlayerPlayPacket packet) {
        // TODO
        // PLACEHOLDER WORK
        playerHandler.readyFor(new File(client.getWorkingFolder(), "source.mp4"));
        playerHandler.play();
    }

    @Override
    public void onPlayerPause(ClientboundPlayerPausePacket packet) {
        if (playerHandler.getPlayer() != null) {
            playerHandler.getPlayer().pause();
        }
    }
}
