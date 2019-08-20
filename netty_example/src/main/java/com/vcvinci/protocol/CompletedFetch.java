package com.vcvinci.protocol;

import com.vcvinci.common.partition.TopicPartition;
import com.vcvinci.protocol.response.FetchResponse;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月7日 上午9:10:47
 * @description 类说明
 */
public class CompletedFetch {

    private TopicPartition partition;
    private long fetchedOffset;
    private FetchResponse.PartitionData partitionData;
    private short version;

    public CompletedFetch(TopicPartition partition, long fetchedOffset, FetchResponse.PartitionData partitionData, short version) {
        this.partition = partition;
        this.fetchedOffset = fetchedOffset;
        this.partitionData = partitionData;
        this.version = version;
    }

    public TopicPartition getPartition() {
        return partition;
    }

    public long getFetchedOffset() {
        return fetchedOffset;
    }

    public FetchResponse.PartitionData getPartitionData() {
        return partitionData;
    }

    public short getVersion() {
        return this.version;
    }

}
