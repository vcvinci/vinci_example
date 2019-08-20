package com.vcvinci.common.exception.server;

/**
 * @Auther: yuxin.chen01
 * @Date: 2018/7/24 20:33
 * @Description: 非消息回溯时, 提交offset比已经提交的offset小
 */
public class InvalidCommitOffsetException extends ServerException {

    public InvalidCommitOffsetException(String message) {
        super(message);
    }

    public InvalidCommitOffsetException(String message, Throwable cause) {
        super(message, cause);
    }
}
