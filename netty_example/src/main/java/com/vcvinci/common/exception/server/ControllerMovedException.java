package com.vcvinci.common.exception.server;

/**
 * @author yuxin.chen01
 * @since 2018/7/4 下午3:03
 */
public class ControllerMovedException extends ServerException {

    public ControllerMovedException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ControllerMovedException(String message) {
        super(message);
    }
}
