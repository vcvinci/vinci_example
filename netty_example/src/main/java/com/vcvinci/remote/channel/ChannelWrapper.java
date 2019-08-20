package com.vcvinci.remote.channel;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelWrapper {

    private Logger log = LoggerFactory.getLogger(ChannelWrapper.class);

    private Channel channel;
    private String addr;
    private long lastReadTime;

    public ChannelWrapper(String addr, Channel channel) {
        this.addr = addr;
        this.channel = channel;
    }

    public boolean isActive() {
        return this.channel != null && this.channel.isActive();
    }

    public boolean isWritable() {
        return this.channel != null && this.channel.isWritable();
    }

    public Channel getChannel() {
        return this.channel;
    }

    public void close(final String cause) {
        log.warn("channel closed cause:" + cause + ",addr:" + this.addr);
        if (this.channel != null) {
            this.channel.close();
        }
    }

    public String getAddr() {
        return this.addr;
    }

    public void refreshLastReadTime(long lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    public long getLastReadTime() {
        return lastReadTime;
    }

}
