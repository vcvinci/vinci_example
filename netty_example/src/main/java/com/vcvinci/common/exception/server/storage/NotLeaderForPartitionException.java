package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;
import com.vcvinci.common.partition.TopicPartition;

/**
 * 本地副本不是分区leader
 *
 * @author yankai.zhang
 * @since 2018/12/19 下午2:54
 */
public class NotLeaderForPartitionException extends DoveProcessibleException {
    public NotLeaderForPartitionException(int replicaId, int leaderId, TopicPartition topicPartition) {
        super("replica " + replicaId + " is not leader "
                + leaderId + " for partition " + topicPartition);
    }
}
