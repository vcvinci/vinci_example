package com.vcvinci.remote.processor;

import com.vcvinci.protocol.network.NetworkSend;
import com.vcvinci.protocol.request.Request;
import com.vcvinci.protocol.request.RequestHeader;
import com.vcvinci.protocol.response.Response;
import com.vcvinci.protocol.util.ProcotolHelper;
import com.vcvinci.protocol.util.RequestResponseMapper;
import com.vcvinci.remote.util.RemoteHelper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @CreateDate: 2018/11/3 14:10
 * @Author: zhengquan.lin@ucarinc.com
 * @Description:
 */
public abstract class AbstractNettyRequestProcessor<T extends Request, R extends Response> implements NettyRequestProcessor<T> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 将request处理结果写入 channel中
     *
     * @param response
     * @param header
     * @param channel
     */
    protected void writeAndFlush(final R response, final RequestHeader header, final Channel channel) {
        if (logger.isDebugEnabled()) {
            logger.debug("服务端响应 RequestId={} , mapper={} 的请求结果", header.getId(), RequestResponseMapper.getMapper(header.getCode()).name());
        }
        final String destination = RemoteHelper.parseChannelRemoteAddr(channel);
        final NetworkSend send = ProcotolHelper.buildNetworkSendOfServer(destination, response, header.getCode(), header.getId());
        // 如果不判断 Channel 的可写状态，可能会导致 OOM 等问题
        if (channel.isWritable()) {
            channel.writeAndFlush(send).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        }
    }
}
