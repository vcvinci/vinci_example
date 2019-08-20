package com.vcvinci.remote.job;

import com.vcvinci.remote.BaseRemote;
import com.vcvinci.remote.channel.ChannelWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author yupeng.sun@ucarinc.com
 * @version 创建时间：2018年1月30日 下午4:42:56
 * @Description
 */
public class ScanIdleChannelJob implements ScanJob {

    private Logger logger = LoggerFactory.getLogger(ScanIdleChannelJob.class);

    private BaseRemote remote;

    public ScanIdleChannelJob(BaseRemote remote) {
        this.remote = remote;
    }

    @Override
    public void scan(long now, ChannelWrapper channel) {
        try {
            List<ChannelWrapper> channels = remote.getAllChannels();
            if (channels == null) {
                return;
            }
            for (final ChannelWrapper ch : channels) {
                if (now - ch.getLastReadTime() > 59000) {
                    ch.close(ch + " is closed by server's scanRunner");
                }
            }

        } catch (Throwable e) {
            logger.error("", "error during scanALLClients on server side:", e);
        }
    }

}
