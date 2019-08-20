package com.vcvinci.remote.handler;

import com.vcvinci.protocol.network.NetworkSend;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyEncoder extends MessageToByteEncoder<NetworkSend> {

    @Override
    protected void encode(ChannelHandlerContext ctx, NetworkSend msg, ByteBuf out) throws Exception {
        out.writeBytes(msg.toByteBuffer());
    }

}
