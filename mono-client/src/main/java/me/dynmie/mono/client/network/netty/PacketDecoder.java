package me.dynmie.mono.client.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.AllArgsConstructor;
import me.dynmie.mono.client.network.NetworkHandler;
import me.dynmie.mono.client.network.connection.ServerConnection;
import me.dynmie.mono.shared.packet.*;
import me.dynmie.mono.shared.utils.SerializationUtils;

/**
 * @author dynmie
 */
@AllArgsConstructor
public class PacketDecoder extends ChannelInboundHandlerAdapter {
    private final NetworkHandler networkHandler;

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

        ConnectionState connectionState = channel.attr(ServerConnection.PROTOCOL_ATTR).get();
        connectionState = connectionState == null ? ConnectionState.HANDSHAKE : connectionState;

        @SuppressWarnings("unchecked") // checked by ConnectionState
        Packet<PacketHandler> packet = (Packet<PacketHandler>) Packets.fromRaw(connectionState, PacketDirection.CLIENTBOUND, rawPacket);

        ServerConnection connection = networkHandler.getConnection();
        PacketHandler packetHandler = connection.getPacketHandler();

        packet.handle(packetHandler);
    }
}
