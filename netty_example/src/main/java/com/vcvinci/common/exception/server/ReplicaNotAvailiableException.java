package com.vcvinci.common.exception.server;

import com.vcvinci.common.exception.api.InvalidMetadataException;

/**
 * @author yuxin.chen01
 * @since 2018/7/4 下午3:03
 */
public class ReplicaNotAvailiableException extends InvalidMetadataException {

    public ReplicaNotAvailiableException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ReplicaNotAvailiableException(String message) {
        super(message);
    }
}
