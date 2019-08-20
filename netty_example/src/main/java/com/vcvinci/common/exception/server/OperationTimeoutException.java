package com.vcvinci.common.exception.server;

/**
 * @author yankai.zhang
 * @since 2018/7/18 下午6:10
 */
public class OperationTimeoutException extends ServerException {

    public OperationTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public OperationTimeoutException(String message) {
        super(message);
    }
}
