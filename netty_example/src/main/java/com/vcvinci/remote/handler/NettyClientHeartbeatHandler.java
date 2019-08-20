package com.vcvinci.remote.handler;

import com.vcvinci.common.exception.client.WriteToChannelException;
import com.vcvinci.protocol.network.NetworkSend;
import com.vcvinci.protocol.request.HeartbeatRequest;
import com.vcvinci.protocol.util.ProcotolHelper;
import com.vcvinci.remote.BaseRemote;
import com.vcvinci.remote.NettyRemoteClient;
import com.vcvinci.remote.RequestWrapper;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author vinci
 * @Date: 2018/6/20 14:57
 * @Description:
 */
public class NettyClientHeartbeatHandler extends AbstractNettyHeartbeatHandler {

    private Logger logger = LoggerFactory.getLogger(NettyRemoteClient.class);

    private BaseRemote client;

    public NettyClientHeartbeatHandler(BaseRemote client) {
        this.client = client;
    }

    @Override
    protected void handleAllIdle(final ChannelHandlerContext ctx) {
        super.handleAllIdle(ctx);
        // 当规定时间没有读写请求访问 consuemr端触发发送心跳请求 todo sendheartbeatRequest
        // todo 需要获取组名 clientId 自动生成得id
        final InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        if (null != socketAddress) {
            HeartbeatRequest request = new HeartbeatRequest((short) 0, "", "", 1, 0);
            final NetworkSend networkSend = ProcotolHelper.buildNetworkSend(socketAddress.getHostName() + ":" + socketAddress.getPort(), request);
            final RequestWrapper requestWrapper = new RequestWrapper(networkSend, request.getCode());

            // 设置requestWrapper
            client.getRequests(ctx.channel()).put(networkSend.getHeader().getId(), requestWrapper);

            ctx.channel().writeAndFlush(networkSend).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) {
                    if (channelFuture.isSuccess()) {
                        client.getRequests(channelFuture.channel()).put(networkSend.getHeader().getId(), requestWrapper);
                    } else {
                        logger.error("send request error when write to channel:" + ctx.channel(), channelFuture.cause());
                        requestWrapper.setException(
                                new WriteToChannelException("3:error to write message to channel", channelFuture.cause()));
                        if (!ctx.channel().isActive()) {
                            client.removeChannel(socketAddress.getHostString() + ":" + socketAddress.getPort());
                        }
                    }
                }
            });
        } else {
            throw new RuntimeException("send heartbeat request, InetSocketAddress is error!");
        }
    }
}
