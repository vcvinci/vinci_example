package com.vcvinci.remote.job;

import com.vcvinci.remote.BaseRemote;
import com.vcvinci.remote.channel.ChannelWrapper;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yupeng.sun@ucarinc.com
 * @version 创建时间：2018年1月26日 下午3:30:29
 * @Description
 */
public class RemoteClientTimerTask implements TimerTask {

    private Logger log = LoggerFactory.getLogger(RemoteClientTimerTask.class);

    private BaseRemote remote;
    private List<ScanJob> jobs;

    public RemoteClientTimerTask(BaseRemote remote, List<ScanJob> jobs) {
        this.remote = remote;
        this.jobs = jobs;
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        List<ChannelWrapper> channels = remote.getAllChannels();
        if (channels == null) {
            return;
        }

        try {
            for (ChannelWrapper channelWrapper : channels) {
                final long now = System.currentTimeMillis();
                for (ScanJob job : jobs) {
                    job.scan(now, channelWrapper);
                }
            }
        } catch (Exception e) {
            log.warn("error to run task.", e);
        } finally {
            remote.getTimer().newTimeout(this, 60, TimeUnit.SECONDS);
        }

    }

}
