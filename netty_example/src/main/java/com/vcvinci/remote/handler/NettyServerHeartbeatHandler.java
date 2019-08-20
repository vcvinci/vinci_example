package com.vcvinci.remote.handler;

import com.vcvinci.remote.BaseRemote;
import com.vcvinci.remote.util.RemoteHelper;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author vinci
 * @Date: 2018/6/20 16:14
 * @Description:
 */
public class NettyServerHeartbeatHandler extends AbstractNettyHeartbeatHandler {

    private BaseRemote client;

    public NettyServerHeartbeatHandler(BaseRemote client) {
        this.client = client;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void handleReaderIdle(ChannelHandlerContext ctx) throws Exception {
        // 服务器端只关心读事件 当规定事件没
        // 有读事件会触发此方法 进行处理
        super.handleReaderIdle(ctx);
        // 针对与这个channel 上得consumer连接断开问题等
        System.err.println("---client " + ctx.channel().remoteAddress().toString() + " reader timeout, close it---");
        //当读空闲被触发时, 模拟关闭channel请求传到上层业务层, 做业务处理
        String channel = RemoteHelper.parseChannelRemoteAddr(ctx.channel());

        ctx.close();
    }
}
