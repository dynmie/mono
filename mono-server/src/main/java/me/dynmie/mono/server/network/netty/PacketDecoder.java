package me.dynmie.mono.server.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.AllArgsConstructor;
import me.dynmie.mono.server.client.ClientHandler;
import me.dynmie.mono.server.client.RemoteClient;
import me.dynmie.mono.server.client.session.SessionHandler;
import me.dynmie.mono.server.network.connection.ClientConnection;
import me.dynmie.mono.server.network.connection.ConnectionHandler;
import me.dynmie.mono.shared.packet.*;
import me.dynmie.mono.shared.session.ClientSession;
import me.dynmie.mono.shared.utils.SerializationUtils;

import java.net.SocketException;
import java.util.logging.Logger;

/**
 * @author dynmie
 */
@AllArgsConstructor
public class PacketDecoder extends ChannelInboundHandlerAdapter {

    private final ConnectionHandler connectionHandler;
    private final SessionHandler sessionHandler;
    private final ClientHandler clientHandler;
    private final Logger logger;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();

        if (!(msg instanceof ByteBuf byteBuf)) {
            super.channelRead(ctx, msg);
            return;
        }

        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        Object object = SerializationUtils.deserialize(bytes);
        if (!(object instanceof RawPacket rawPacket)) {
            super.channelRead(ctx, msg);
            return;
        }

        // PACKET
        ConnectionState connectionState = channel.attr(ClientConnection.PROTOCOL_ATTR).get();
        if (connectionState == null) {
            connectionState = ConnectionState.HANDSHAKE;
        }

        @SuppressWarnings("unchecked") // checked by ConnectionState
        Packet<PacketHandler> packet = (Packet<PacketHandler>) Packets.fromRaw(connectionState, PacketDirection.SERVERBOUND, rawPacket);

        ClientConnection clientConnection = connectionHandler.getConnectionByChannel(channel);
        if (clientConnection == null) {
            clientConnection = connectionHandler.createConnection(channel);
            connectionHandler.addConnection(clientConnection);
        }

        PacketHandler handler = clientConnection.getPacketHandler();

        packet.handle(handler);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();

        ClientConnection connection = connectionHandler.getConnectionByChannel(channel);
        if (connection == null) {
            connectionHandler.removeConnectionByChannel(channel);
            return;
        }

        // SESSION INFO
        ClientSession session = sessionHandler.getSessionByChannel(channel);
        if (session == null) {
            connectionHandler.removeConnectionByChannel(channel);
            logger.info("Unknown client from " + channel.remoteAddress() + " disconnected from the server.");
            return;
        }

        RemoteClient client = clientHandler.getClient(session.getUniqueId());
        clientHandler.removeClient(client);

        logger.info("Client %s[%s](%s) disconnected from the server.".formatted(
                session.getName(),
                session.getUniqueId(),
                channel.remoteAddress()
        ));

        connectionHandler.removeConnectionByChannel(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof SocketException) {
            // Connection reset
            return;
        }

        super.exceptionCaught(ctx, cause);
    }
}
