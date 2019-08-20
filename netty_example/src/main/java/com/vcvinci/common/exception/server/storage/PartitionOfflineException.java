package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;
import com.vcvinci.common.partition.TopicPartition;

/**
 * @author yankai.zhang
 * @since 2018/12/28 上午8:40
 */
public class PartitionOfflineException extends DoveProcessibleException {

    public PartitionOfflineException(TopicPartition tp, int brokerId) {
        super("partition " + tp + " is offline on broker" + brokerId);
    }
}
