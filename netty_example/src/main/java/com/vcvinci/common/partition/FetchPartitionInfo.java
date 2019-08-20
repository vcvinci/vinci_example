package com.vcvinci.common.partition;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 上午7:29:17
 * @description 类说明
 */
public class FetchPartitionInfo {

    private long fetchOffset;
    private int maxBytes;
    private int position;

    public long getFetchOffset() {
        return fetchOffset;
    }

    public void setFetchOffset(long fetchOffset) {
        this.fetchOffset = fetchOffset;
    }

    public int getMaxBytes() {
        return maxBytes;
    }

    public void setMaxBytes(int maxBytes) {
        this.maxBytes = maxBytes;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
