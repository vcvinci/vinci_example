package com.vcvinci.common.exception.server;

/**
 * 本地写入消息出错通用异常
 *
 * @author yankai.zhang
 * @since 2018/7/4 下午5:18
 */
public class LocalReadException extends ServerException {

    public LocalReadException(String message) {
        super(message);
    }

    public LocalReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
