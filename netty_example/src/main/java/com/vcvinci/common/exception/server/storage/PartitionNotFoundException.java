package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;
import com.vcvinci.common.partition.TopicPartition;

/**
 * @author yankai.zhang
 * @since 2019/1/8 下午5:30
 */
public class PartitionNotFoundException extends DoveProcessibleException {
    public PartitionNotFoundException(TopicPartition tp, int brokerId) {
        super("partition " + tp + " not found on broker " + brokerId);
    }
}
