package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;
import com.vcvinci.common.partition.TopicPartition;

/**
 * 本地副本没跟上同步进度
 *
 * @author yankai.zhang
 * @since 2018/12/28 下午3:42
 */
public class NotInSRException extends DoveProcessibleException {
	public NotInSRException(int replicaId, TopicPartition topicPartition) {
		super("replica " + replicaId + " is not in sr of partition " + topicPartition);
	}
}
