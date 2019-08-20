package com.vcvinci.common.exception.server;

/**
 * 本地读取消息出错通用异常
 *
 * @author yankai.zhang
 * @since 2018/7/4 下午5:18
 */
public class LocalAppendException extends ServerException {

    public LocalAppendException(String message) {
        super(message);
    }

    public LocalAppendException(String message, Throwable cause) {
        super(message, cause);
    }
}
