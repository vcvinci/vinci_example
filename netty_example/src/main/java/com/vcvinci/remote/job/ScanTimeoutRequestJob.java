package com.vcvinci.remote.job;

import com.vcvinci.common.exception.client.TimeoutException;
import com.vcvinci.remote.BaseRemote;
import com.vcvinci.remote.RequestWrapper;
import com.vcvinci.remote.channel.ChannelWrapper;
import io.netty.channel.Channel;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yupeng.sun@ucarinc.com
 * @version 创建时间：2018年1月26日 下午2:50:49
 * @Description
 */
public class ScanTimeoutRequestJob implements ScanJob {

    private BaseRemote remote;

    public ScanTimeoutRequestJob(BaseRemote remote) {
        this.remote = remote;
    }

    @Override
    public void scan(long now, ChannelWrapper channelWrapper) {
        if (channelWrapper.isActive()) {
            cleanTimeoutRequest(channelWrapper.getChannel(), now);
        } else {
            channelWrapper.close("close by ScanTimeoutRequestJob,cause channel is inActive");
        }
    }

    private void cleanTimeoutRequest(final Channel channel, long jobStartTime) {
        ConcurrentHashMap<Integer, RequestWrapper> requestWrapper = remote.getRequests(channel);
        for (Entry<Integer, RequestWrapper> entry : requestWrapper.entrySet()) {
            if (entry.getValue().getTimeout() < jobStartTime - entry.getValue().getRequestTime()) {
                requestWrapper.remove(entry.getKey());
                entry.getValue().setException(
                        new TimeoutException("code:2,reason:request timeout"));
            }
        }
    }

}
