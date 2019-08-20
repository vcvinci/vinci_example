package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;
import com.vcvinci.common.partition.TopicPartition;

/**
 * 本地副本不存在
 *
 * @author yankai.zhang
 * @since 2018/12/28 下午2:45
 */
public class ReplicaNotFoundException extends DoveProcessibleException {
	public ReplicaNotFoundException(int brokerId, int replicaId, TopicPartition topicPartition) {
		super("replica " + replicaId + " not found for partition " + topicPartition + " on broker " + brokerId);
	}
}
