package me.dynmie.mono.client.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import me.dynmie.mono.shared.util.SerializationUtils;

import java.io.Serializable;

/**
 * @author dynmie
 */
public class SerializableEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof Serializable serializable)) {
            super.write(ctx, msg, promise);
            return;
        }
        byte[] bytes = SerializationUtils.serialize(serializable);
        ByteBuf byteBuf = ctx.alloc().buffer(bytes.length);
        byteBuf.writeBytes(bytes);
        ctx.write(byteBuf);
    }
}
