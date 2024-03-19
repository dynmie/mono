package me.dynmie.mono.server.network.handler;

import me.dynmie.jeorge.Inject;
import me.dynmie.jeorge.Injector;
import me.dynmie.mono.server.client.RemoteClient;
import me.dynmie.mono.server.command.handler.CommandHandler;
import me.dynmie.mono.server.network.connection.ClientConnection;
import me.dynmie.mono.shared.packet.ready.ServerboundReadyPacketHandler;
import me.dynmie.mono.shared.packet.ready.server.ServerboundPlayerInfoPacket;
import me.dynmie.mono.shared.packet.ready.server.ServerboundPlayerPlaylistUpdatePacket;

/**
 * @author dynmie
 */
public class DefaultServerboundReadyPacketHandler implements ServerboundReadyPacketHandler {
    private final RemoteClient client;
    private final ClientConnection connection;

    @Inject
    private CommandHandler commandHandler;

    public DefaultServerboundReadyPacketHandler(Injector injector, RemoteClient client, ClientConnection connection) {
        this.client = client;
        this.connection = connection;

        injector.injectMembers(this);
    }

    @Override
    public void onPlaylistUpdate(ServerboundPlayerPlaylistUpdatePacket packet) {
        client.setPlaylistInfo(packet.getPlaylistInfo());
    }

    @Override
    public void onPlayerInfo(ServerboundPlayerInfoPacket packet) {
        client.setPlayerInfo(packet.getPlayerInfo());
        commandHandler.execute("np " + client.getUniqueId());
    }
}
