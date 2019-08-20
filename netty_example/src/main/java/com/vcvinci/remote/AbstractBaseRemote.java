package com.vcvinci.remote;

import com.vcvinci.common.annotation.Nullable;
import com.vcvinci.common.thread.NamedThreadFactory;
import com.vcvinci.protocol.request.Request;
import com.vcvinci.remote.config.AbstractConfig;
import com.vcvinci.remote.job.ScanJob;
import com.vcvinci.remote.job.ScanTimeoutRequestJob;
import com.vcvinci.remote.processor.NettyRequestProcessor;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import io.netty.util.internal.SystemPropertyUtil;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBaseRemote implements BaseRemote {

    private final Timer timer = new HashedWheelTimer(new NamedThreadFactory("dove_remote_timer"));

    private final NioEventLoopGroup workerGroup = new NioEventLoopGroup(
            SystemPropertyUtil.getInt("dove.workers", Runtime.getRuntime().availableProcessors() * 2),
            new NamedThreadFactory("dove-worker"));

    private final Map<Short, NettyRequestProcessor<? extends Request>> requestProcessors = new HashMap<>();

    private final ByteBufAllocator byteBufAllocator;

    // 缓存通道， 每个通道有多个请求
    private ConcurrentHashMap<Channel, ConcurrentHashMap<Integer/* requestId */, RequestWrapper>> channel2RequestMaps = new ConcurrentHashMap<>();

    public AbstractBaseRemote(AbstractConfig config) {
        if (config.isBytebufPool()) {
            byteBufAllocator = PooledByteBufAllocator.DEFAULT;
        } else {
            byteBufAllocator = UnpooledByteBufAllocator.DEFAULT;
        }
        List<ScanJob> jobs = new ArrayList<ScanJob>();
        jobs.add(new ScanTimeoutRequestJob(this));
        //getTimer().newTimeout(new RemoteClientTimerTask(this, jobs), 60, TimeUnit.SECONDS);
    }

    @Override
    public void registerProcessor(short requestCode, NettyRequestProcessor<? extends Request> processor) {
        requestProcessors.put(requestCode, processor);
    }

    @Override
    public Timer getTimer() {
        return this.timer;
    }

    @Override
    public NettyRequestProcessor<? extends Request> getProcessor(short requestCode) {
        return this.requestProcessors.get(requestCode);
    }

    public NioEventLoopGroup getWorkerGroup() {
        return this.workerGroup;
    }

    public ByteBufAllocator getByteBufAllocator() {
        return this.byteBufAllocator;
    }

    /**
     * channelActive 的时候，将Channel作为key缓存起来
     *
     * @param channel
     */
    @Override
    public void initRequestMap(Channel channel) {
        channel2RequestMaps.put(channel, new ConcurrentHashMap<Integer, RequestWrapper>());
    }

    @Override
    public ConcurrentHashMap<Integer/* requestId */, RequestWrapper> removeRequests(Channel channel) {
        return channel2RequestMaps.remove(channel);
    }

    @Override
    public ConcurrentHashMap<Integer/* requestId */, RequestWrapper> getRequests(Channel channel) {
        return channel2RequestMaps.get(channel);
    }

    @Override
    @Nullable
    public String availableHostOfRandom() {
        Set<Channel> channels = channel2RequestMaps.keySet();
        Iterator<Channel> it = channels.iterator();
        if (!it.hasNext()) {
            return null;
        }
        InetSocketAddress address = (InetSocketAddress) it.next().remoteAddress();
        return address.getHostString() + ":" + address.getPort();
    }
}
