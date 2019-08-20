package com.vcvinci.common.exception.server;

/**
 * @CreateDate: 2018/12/22 10:36
 * @Author: zhengquan.lin@ucarinc.com
 * @Description:
 */
public class NotCoordinatorException extends ServerException {

    public NotCoordinatorException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public NotCoordinatorException(String message) {
        super(message);
    }
}
