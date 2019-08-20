package com.vcvinci.remote;

import com.vcvinci.protocol.request.Request;
import com.vcvinci.remote.channel.ChannelWrapper;
import com.vcvinci.remote.processor.NettyRequestProcessor;
import io.netty.channel.Channel;
import io.netty.util.Timer;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 远程通讯基础接口
 * @author yupeng.sun@ucarinc.com
 * @version 1.0 创建时间：2017-12-21 上午10:29:01
 */
public interface BaseRemote {

    /**
     * 功能描述: 连接上的所有channel信息
     * @return 返回所有channel信息
     * @throws Exception 网络问题
     */
    List<ChannelWrapper> getAllChannels() throws Exception;

    /**
     * 功能描述:
     * @param url ip:port
     */
    void removeChannel(String url);

    /**
     * 功能描述: 开启远程控制
     * @throws Exception 网络问题
     */
    void start() throws Exception;

    /**
     * 功能描述: 开启远程控制
     * @param requestCode api code
     * @param processor 处理器
     */
    void registerProcessor(final short requestCode, final NettyRequestProcessor<? extends Request> processor);

    NettyRequestProcessor<? extends Request> getProcessor(short requestCode);

    void shutdown();

    Timer getTimer();

    void initRequestMap(Channel channel);

    ConcurrentHashMap<Integer/* requestId */, RequestWrapper> removeRequests(Channel channel);

    ConcurrentHashMap<Integer/* requestId */, RequestWrapper> getRequests(Channel channel);

    String availableHostOfRandom();

}
