package com.vcvinci.common.exception.server;

/**
 * @Auther: yuxin.chen01
 * @Date: 2018/7/9 17:15
 * @Description: group内未知的member
 */
public class UnknownMemberException extends ServerException {

    public UnknownMemberException(String message) {
        super(message);
    }

    public UnknownMemberException(String message, Throwable cause) {
        super(message, cause);
    }
}
