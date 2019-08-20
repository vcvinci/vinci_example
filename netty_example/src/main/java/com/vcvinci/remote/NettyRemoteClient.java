package com.vcvinci.remote;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.vcvinci.common.exception.client.WriteToChannelException;
import com.vcvinci.protocol.network.NetworkSend;
import com.vcvinci.protocol.request.Request;
import com.vcvinci.protocol.request.RequestHeader;
import com.vcvinci.protocol.response.Response;
import com.vcvinci.protocol.util.ProcotolHelper;
import com.vcvinci.remote.callback.RequestCompletionHandler;
import com.vcvinci.remote.callback.RequestFuture;
import com.vcvinci.remote.callback.RequestFutureListener;
import com.vcvinci.remote.channel.ChannelWrapper;
import com.vcvinci.remote.config.NettyClientConfig;
import com.vcvinci.remote.handler.NettyBusinessHandler;
import com.vcvinci.remote.handler.NettyDecoder;
import com.vcvinci.remote.handler.NettyEncoder;
import com.vcvinci.remote.stats.RuntimeStats;
import com.vcvinci.remote.util.RemoteUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NettyRemoteClient extends AbstractBaseRemote implements RemoteClient {

    private Logger logger = LoggerFactory.getLogger(NettyRemoteClient.class);
    private NettyClientConfig config;
    private Bootstrap bootstrap;

    public NettyRemoteClient(NettyClientConfig config) {
        super(config);
        this.config = config;
    }

    private final Cache<String, ChannelWrapper> cacheChannels = CacheBuilder.newBuilder()
            // 最大10万个
            .maximumSize(100000)
            // 如果经过30分钟没有访问，释放掉连接，缓解内存和服务器连接压力；
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .removalListener(new RemovalListener<String, ChannelWrapper>() {
                @Override
                public void onRemoval(RemovalNotification<String, ChannelWrapper> notification) {
                    if (notification.getValue().isActive()) {
                        notification.getValue().close("channel removed from cache : " + notification.getCause());
                    }
                }
            }).build();

    @Override
    public void start() {
        try {
            bootstrap = new Bootstrap()
                    .group(getWorkerGroup())
                    // default true
                    .option(ChannelOption.TCP_NODELAY, config.isTcpNoDelay())
                    .option(ChannelOption.SO_REUSEADDR, config.isSoReuseaddr())
                    // default true
                    .option(ChannelOption.SO_KEEPALIVE, config.isSoKeepalive())
                    .option(ChannelOption.ALLOCATOR, getByteBufAllocator())
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                                 @Override
                                 protected void initChannel(SocketChannel socketChannel) throws Exception {
                                     socketChannel.pipeline().addLast(
                                             new LengthFieldPrepender(4),
                                             new NettyEncoder(),
                                             new NettyDecoder(),
                                             new IdleStateHandler(config.getReaderIdleTimeSeconds(), config.getWriterIdleTimeSeconds(),
                                                     config.getAllIdleTimeSeconds()),
                                             new NettyBusinessHandler(NettyRemoteClient.this)
                                     );
                                 }
                             }
                    );
        } catch (Exception e) {
            logger.error("error to start netty remote client.", e);
        }
    }

    @Override
    public void shutdown() {
        try {
            this.getTimer().stop();

            for (ChannelWrapper cw : this.cacheChannels.asMap().values()) {
                RemoteUtil.closeChannel(cw.getChannel());
            }
            this.cacheChannels.invalidateAll();
            this.getWorkerGroup().shutdownGracefully();

        } catch (Exception e) {
            logger.error("NettyRemotingClient shutdown exception, ", e);
        }
    }

    @Override
    public Response invokeSync(String addr, final Request request, long timeout) throws TimeoutException, InterruptedException {
        final NetworkSend networkSend = ProcotolHelper.buildNetworkSend(addr, request);
        final Channel channel = getOrCreateChannel(addr, timeout);
        // 发送请求
        RequestFuture<Response> future = getResponseRequestFuture(channel, networkSend, request);
        boolean done = future.awaitDone(timeout, TimeUnit.MILLISECONDS);
        if (!done) {
            // prevent memory leak
            getRequests(channel).remove(networkSend.getHeader().getId());
            throw new TimeoutException(timeout + " ms");
        }
        if (future.isSucceeded()) {
            return future.value();
        } else {
            throw future.exception();
        }
    }

    @Override
    public void invokeAsync(String addr, Request request, long timeoutMillis, final RequestCompletionHandler callback) throws TimeoutException {
        // 发送请求
        final NetworkSend networkSend = ProcotolHelper.buildNetworkSend(addr, request);
        final Channel channel = getOrCreateChannel(addr, timeoutMillis);
        RequestFuture<Response> future = getResponseRequestFuture(channel, networkSend, request);
        future.addListener(new RequestFutureListener<Response>() {
            @Override
            public void onSuccess(Response value) {
                if (callback != null) {
                    callback.onComplete(value, ProcotolHelper.parseToResponseHeader((RequestHeader) networkSend.getHeader()));
                } else {
                    logger.warn("Callback is null!!!");
                }
            }

            @Override
            public void onFailure(RuntimeException e) {
                if (callback != null) {
                    callback.onFailure(e);
                } else {
                    logger.warn("Callback is null!!!");
                }
            }
        });
    }

    @Override
    public List<ChannelWrapper> getAllChannels() {
        List<ChannelWrapper> result = new ArrayList<ChannelWrapper>((int) cacheChannels.size());
        result.addAll(cacheChannels.asMap().values());
        return result;
    }

    @Override
    public boolean connected(String host) {
        if (null == this.cacheChannels.getIfPresent(host)) {
            return false;
        }
        return true;
    }

    @Override
    public void removeChannel(String url) {
        if (url == null) {
            return;
        }
        ChannelWrapper ifPresent = cacheChannels.getIfPresent(url);
        if (null != ifPresent) {
            RemoteUtil.closeChannel(ifPresent.getChannel());
        }
        cacheChannels.invalidate(url);
    }

    @Override
    public void connect(String host, long timeout) throws TimeoutException{
        this.getOrCreateChannel(host, timeout);
    }

    @Override
    public void connect(String host) throws TimeoutException {
        connect(host, config.getConnectTimeoutMs());
    }

    private Channel getOrCreateChannel(String addr, long timeout) throws TimeoutException {
        ChannelWrapper warpper = getChannelWarpper(addr, timeout);
        if (warpper != null) {
            return warpper.getChannel();
        }
        throw new TimeoutException("error to get channel");
    }

    private ChannelWrapper getChannelWarpper(final String addr, final long timeout) {
        ChannelWrapper channelWarpper;
        try {
            channelWarpper = cacheChannels.get(addr, new Callable<ChannelWrapper>() {
                @Override
                public ChannelWrapper call() throws TimeoutException {
                    return createChannelWarpper(addr, timeout);
                }
            });
        } catch (ExecutionException e) {
            logger.warn("cacheChannels get is error.", e);
            return null;
        }
        if (channelWarpper == null || !channelWarpper.isActive()) {
            this.removeChannel(addr);
            return null;
        }
        return channelWarpper;
    }

    // todo vinci timout deal
    private ChannelWrapper createChannelWarpper(final String address, long timeout) throws TimeoutException {
        String[] strArr = RemoteUtil.parseStr(address, ":");
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(strArr[0], Integer.valueOf(strArr[1])));
        if (future.awaitUninterruptibly(config.getConnectTimeoutMs()) && future.isSuccess() && future.channel().isActive()) {
            Channel channel = future.channel();
            initRequestMap(channel);
            logger.info("Connect to {} server successfully!", address);
            return new ChannelWrapper(address, channel);
        } else {
            future.cancel(true);
            future.channel().close();
            //logger.warn("Failed to connect to server {}, try connect after 10s", address);
            throw new TimeoutException("Fail to connect:" + address + ", timeout: " + timeout + " .");
        }
    }

    private RequestFuture<Response> getResponseRequestFuture(final Channel channel, final NetworkSend networkSend,  Request request) {
        final RequestWrapper requestWrapper = new RequestWrapper(networkSend, request.getCode());
        getRequests(channel).put(networkSend.getHeader().getId(), requestWrapper);
        channel.writeAndFlush(networkSend).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) {
                if (!channelFuture.isSuccess()) {
                    getRequests(channel).remove(networkSend.getHeader().getId());
                    NettyRemoteClient.this.operationFailure(channelFuture, requestWrapper, channel);
                }
            }
        });
        RuntimeStats.getInstance().increaseSendRequestCount();
        return requestWrapper.getFuture();
    }

    /**
     * 功能描述: channel写出消息得回调完成处理
     *
     * @param: [channelFuture, networkSend, requestWrapper, channel]
     * @return: void
     * @author vinci
     * @date: 2018/7/5 15:42
     */
    private void operationFailure(ChannelFuture channelFuture, RequestWrapper requestWrapper, Channel channel) {
            logger.error("Send request error when write to channel:" + channel, channelFuture.cause());
            requestWrapper.setException(new WriteToChannelException("Error to write message to channel", channelFuture.cause()));
            if (channel.isActive()) {
                InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
                if (address != null) {
                    removeChannel(address.getHostString() + ":" + address.getPort());
                }
            }
    }

}
