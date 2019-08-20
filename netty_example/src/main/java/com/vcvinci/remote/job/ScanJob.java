package com.vcvinci.remote.job;

import com.vcvinci.remote.channel.ChannelWrapper;

/**
 * @author yupeng.sun@ucarinc.com
 * @version 创建时间：2018年1月26日 下午2:48:21
 * @Description
 */
public interface ScanJob {

    public void scan(long now, ChannelWrapper channel);

}
