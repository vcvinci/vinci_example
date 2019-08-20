package com.vcvinci.remote;

import com.vcvinci.common.exception.client.WriteToChannelException;
import com.vcvinci.common.thread.NamedThreadFactory;
import com.vcvinci.protocol.network.NetworkSend;
import com.vcvinci.protocol.request.Request;
import com.vcvinci.protocol.response.Response;
import com.vcvinci.protocol.util.ProcotolHelper;
import com.vcvinci.remote.callback.RequestFuture;
import com.vcvinci.remote.channel.ChannelWrapper;
import com.vcvinci.remote.config.NettyServerConfig;
import com.vcvinci.remote.handler.NettyBusinessHandler;
import com.vcvinci.remote.handler.NettyDecoder;
import com.vcvinci.remote.handler.NettyEncoder;
import com.vcvinci.remote.stats.RuntimeStats;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class NettyRemoteServer extends AbstractBaseRemote implements RemoteServer {

    private Logger logger = LoggerFactory.getLogger(NettyRemoteServer.class);
    private ServerBootstrap serverBootstrap;
    private final EventLoopGroup eventLoopGroupBoss;
    private final EventLoopGroup eventLoopGroupSelector;
    private final NettyServerConfig nettyServerConfig;
    private final AtomicBoolean start = new AtomicBoolean(false);
    private NettyBusinessHandler handler;

    public NettyRemoteServer(final NettyServerConfig nettyServerConfig) {
        super(nettyServerConfig);
        this.serverBootstrap = new ServerBootstrap();
        this.eventLoopGroupBoss = new NioEventLoopGroup(1, new NamedThreadFactory("nettyBoss"));
        this.eventLoopGroupSelector = new NioEventLoopGroup(nettyServerConfig.getServerWorkerCount(), new NamedThreadFactory("nettySelector"));
        this.nettyServerConfig = nettyServerConfig;
        handler = new NettyBusinessHandler(this);
    }

    @Override
    public void start() throws InterruptedException {
        if (!start.compareAndSet(false, true)) {
            logger.warn("Server is already started!");
            return;
        }

        int port = nettyServerConfig.getPort();
        this.serverBootstrap.group(this.eventLoopGroupBoss, this.eventLoopGroupSelector)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, nettyServerConfig.getSoBacklog())
                .option(ChannelOption.SO_REUSEADDR, nettyServerConfig.isSoReuseaddr())
                .childOption(ChannelOption.SO_KEEPALIVE, nettyServerConfig.isSoKeepalive())
                .childOption(ChannelOption.TCP_NODELAY, nettyServerConfig.isTcpNoDelay())
                .childOption(ChannelOption.ALLOCATOR, getByteBufAllocator())
                .childOption(ChannelOption.SO_SNDBUF, nettyServerConfig.getServerSocketSndBufSize())
                .childOption(ChannelOption.SO_RCVBUF, nettyServerConfig.getServerSocketRcvBufSize())
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                new LengthFieldPrepender(4),
                                new NettyEncoder(),
                                new NettyDecoder(),
                                new IdleStateHandler(nettyServerConfig.getReaderIdleTimeSeconds(),
                                        nettyServerConfig.getWriterIdleTimeSeconds(),
                                        nettyServerConfig.getServerChannelMaxIdleTimeSeconds()),
                                NettyRemoteServer.this.handler
                        );
                    }
                });
        try {
            ChannelFuture cf = this.serverBootstrap.bind().sync();
            if (cf.isSuccess()) {
                logger.warn("Server started,listen at: " + port);
                return;
            } else {
                logger.error("attempts to start server at port : " + port);
                throw new RuntimeException("attempts to start server at port : " + port);
            }
        } catch (Exception e) {
            throw new RuntimeException("this.serverBootstrap.bind().sync() InterruptedException", e);
        }

    }

    @Override
    public void shutdown() {
        logger.warn("Server stop!");
        this.start.set(false);
        try {
            this.eventLoopGroupBoss.shutdownGracefully();
            this.eventLoopGroupSelector.shutdownGracefully();
        } catch (Exception e) {
            logger.error("NettyRemoteServer shutdown exception, ", e);
        }
    }

    @Override
    public int localListenPort() {
        return this.nettyServerConfig.getPort();
    }

    @Override
    public Response invokeSync(final Channel channel, Request request, long timeoutMillis) throws InterruptedException {
        // 发送请求
        if (channel == null) {
            throw new RuntimeException("error to get channel,channel is null");
        }
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        if (socketAddress == null) {
            throw new RuntimeException("error to get channel remote address");
        }
        final String address = socketAddress.getHostName() + ":" + socketAddress.getPort();
        final NetworkSend networkSend = ProcotolHelper.buildNetworkSend(address, request);
        final RequestWrapper requestWrapper = new RequestWrapper(networkSend, request.getCode());

        channel.writeAndFlush(networkSend).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    getRequests(channelFuture.channel()).put(networkSend.getHeader().getId(), requestWrapper);
                } else {
                    logger.error("send request error when write to channel:" + channel, channelFuture.cause());
                    requestWrapper.setException(
                            new WriteToChannelException("3:error to write message to channel", channelFuture.cause()));
                    if (!channel.isActive()) {
                        removeChannel(address);
                    }
                }
            }
        });
        RuntimeStats.getInstance().increaseSendRequestCount();
        RequestFuture<Response> future = requestWrapper.getFuture();
        future.awaitDone(timeoutMillis, TimeUnit.MILLISECONDS);
        if (future.isSucceeded()) {
            return future.value();
        } else {
            throw future.exception();
        }
    }

    @Override
    public List<ChannelWrapper> getAllChannels() throws Exception {
        return this.handler.listChannels();
    }

    @Override
    public void removeChannel(String url) {
        this.handler.removeChannel(url);
    }

}
