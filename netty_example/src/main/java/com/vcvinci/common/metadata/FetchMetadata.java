package com.vcvinci.common.metadata;

import com.vcvinci.common.partition.TopicPartition;

import java.util.Map;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月5日 上午10:05:22
 * @description 类说明
 */
public class FetchMetadata {

    private int fetchMinBytes;
    private int fetchMaxBytes;
    private int replicaId;
    private boolean onlyLeader;
    private Map<TopicPartition, FetchPartitionMetadata> fetchInfos;
}
 