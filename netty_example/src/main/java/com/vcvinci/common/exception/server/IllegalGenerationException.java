package com.vcvinci.common.exception.server;

/**
 * @Auther: yuxin.chen01
 * @Date: 2018/7/12 20:57
 * @Description: 同步组请求时,
 */
public class IllegalGenerationException extends ServerException {

    public IllegalGenerationException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public IllegalGenerationException(String message) {
        super(message);
    }
}
