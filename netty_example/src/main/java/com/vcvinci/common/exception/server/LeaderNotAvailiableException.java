package com.vcvinci.common.exception.server;

import com.vcvinci.common.exception.api.InvalidMetadataException;

/**
 * @author yuxin.chen01
 * @since 2018/7/4 下午3:03
 */
public class LeaderNotAvailiableException extends InvalidMetadataException {

    public LeaderNotAvailiableException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public LeaderNotAvailiableException(String message) {
        super(message);
    }
}
