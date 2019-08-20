package com.vcvinci.common.exception.api;

/**
 * @Auther: yuxin.chen01
 * @Date: 2018/7/9 16:38
 * @Description:
 */
public class InvalidSessionTimeoutException extends ApiException {

    public InvalidSessionTimeoutException() {
    }

    public InvalidSessionTimeoutException(String message) {
        super(message);
    }

    public InvalidSessionTimeoutException(Throwable cause) {
        super(cause);
    }

    public InvalidSessionTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
