package com.vcvinci.remote.handler;

import com.vcvinci.common.exception.client.ConnectClosedException;
import com.vcvinci.protocol.network.NetworkReceive;
import com.vcvinci.protocol.request.Request;
import com.vcvinci.protocol.request.RequestHeader;
import com.vcvinci.protocol.response.Response;
import com.vcvinci.remote.BaseRemote;
import com.vcvinci.remote.RequestWrapper;
import com.vcvinci.remote.channel.ChannelWrapper;
import com.vcvinci.remote.processor.NettyRequestProcessor;
import com.vcvinci.remote.stats.RuntimeStats;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能描述: 网络传输业务处理器
 * @author vinci
 * @date 2018/11/29 下午2:api
 */
@ChannelHandler.Sharable
public class NettyBusinessHandler extends ChannelDuplexHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyBusinessHandler.class);
    private ConcurrentHashMap<String, ChannelWrapper> cacheChannels = new ConcurrentHashMap<>();
    private BaseRemote client;

    public NettyBusinessHandler(BaseRemote client) {
        this.client = client;
    }

    /**
     * 由 {@link NettyDecoder} 处理、转换为 NetworkReceive 并传递给下一个 Handler
     *
     * @param ctx
     * @param obj
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        NetworkReceive receive = (NetworkReceive) obj;
        if (receive.isResponse()) {
            processResponse(ctx, receive);
        } else {
            processRequest(ctx, receive);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.warn("there is some exception with channel:" + ctx.channel(), cause);
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("connection connected: " + ctx.channel());
        RuntimeStats.getInstance().increaseConnAsClientCount();
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        LOGGER.warn("connection closed: " + channel);
        RuntimeStats.getInstance().decreaseConnAsClientCount();
        InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
        String url = address.getHostString() + ":" + address.getPort();
        ConcurrentHashMap<Integer, RequestWrapper> requests = client.removeRequests(channel);
        if (MapUtils.isNotEmpty(requests)) {
            for (final Map.Entry<Integer, RequestWrapper> entry : requests.entrySet()) {
                entry.getValue().setException(new ConnectClosedException("connection closed"));
            }
        }
        client.removeChannel(url);
        super.channelInactive(ctx);
    }

    /**
     * 功能描述: 处理请求
     * @param ctx netty object
     * @param receive 接收请求对象
     * @throws Exception 网络问题
     * @author vinci
     * @date 2018/11/29 下午3:08
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void processRequest(ChannelHandlerContext ctx, NetworkReceive receive) throws Exception {
        RequestHeader header = (RequestHeader) receive.getHeader();
        Request req = (Request) receive.buildBody(header);
        // 处理关闭连接的Processor
        NettyRequestProcessor processor = this.client.getProcessor(req.getCode());
        processor.processRequest(req, header, ctx.channel());
    }

    /**
     * 功能描述: 处理响应
     * @param ctx netty object
     * @param receive 接收到的对象
     * @author vinci
     * @date 2018/11/29 下午3:18 
     */
    private void processResponse(ChannelHandlerContext ctx, NetworkReceive receive) {
        RequestWrapper requestWrapper = client.getRequests(ctx.channel()).remove(receive.getId());
        if (requestWrapper == null) {
            String msg = "this is no request for response,maybe timeout,requestId:" + receive.getId();
            LOGGER.warn(msg);
            return;
        }
        try {
            RequestHeader requestHeader = (RequestHeader) requestWrapper.getHeader();
            Response resp = (Response) receive.buildBody(requestHeader);
            requestWrapper.onResponse(resp);
        } catch (RuntimeException e) {
            requestWrapper.setException(e);
        }
    }

    public List<ChannelWrapper> listChannels() {
        List<ChannelWrapper> result = new ArrayList<>(cacheChannels.size());
        result.addAll(cacheChannels.values());
        return result;
    }

    public void removeChannel(String url) {
        this.cacheChannels.remove(url);
    }
}
