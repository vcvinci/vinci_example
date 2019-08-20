package com.vcvinci.common.exception.server;

import com.vcvinci.common.exception.api.InvalidMetadataException;

/**
 * topic或分区在服务端不存在
 *
 * @author yankai.zhang
 * @since 2018/6/26 下午3:03
 */
public class UnknownTopicOrPartitionException extends InvalidMetadataException {

    public UnknownTopicOrPartitionException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public UnknownTopicOrPartitionException(String message) {
        super(message);
    }
}
