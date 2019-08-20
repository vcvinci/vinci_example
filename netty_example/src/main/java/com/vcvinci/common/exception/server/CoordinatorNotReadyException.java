package com.vcvinci.common.exception.server;

import com.vcvinci.common.exception.RetriableException;

/**
 * @Auther: yuxin.chen01
 * @Date: 2018/7/9 16:22
 * @Description:
 */
public class CoordinatorNotReadyException extends ServerException implements RetriableException{

    public CoordinatorNotReadyException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public CoordinatorNotReadyException(String message) {
        super(message);
    }
}
