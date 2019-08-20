package com.vcvinci.remote.handler;

import com.vcvinci.protocol.network.NetworkReceive;
import com.vcvinci.remote.util.RemoteHelper;
import com.vcvinci.remote.util.RemoteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class NettyDecoder extends LengthFieldBasedFrameDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyDecoder.class);

    private static final int FRAME_MAX_LENGTH = 10 * 1024 * 1024;

    public NettyDecoder() {
        super(FRAME_MAX_LENGTH, 0, 4, 0, 4);
    }

    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = null;
        try {
            frame = (ByteBuf) super.decode(ctx, in);
            if (null == frame) {
                return null;
            }

            ByteBuffer byteBuffer = ByteBuffer.allocate(frame.readableBytes());
            frame.readBytes(byteBuffer);
            byteBuffer.flip();
            String source = RemoteHelper.parseChannelRemoteAddr(ctx.channel());
            return new NetworkReceive(source, byteBuffer);
        } catch (Exception e) {
            LOGGER.error("decode exception, " + RemoteHelper.parseChannelRemoteAddr(ctx.channel()), e);
            RemoteUtil.closeChannel(ctx.channel());
        } finally {
            if (null != frame) {
                frame.release();
            }
        }

        return null;
    }

}
