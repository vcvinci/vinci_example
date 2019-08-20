package com.vcvinci.common.exception.server;

import com.vcvinci.common.exception.RetriableException;

/**
 * @Auther: yuxin.chen01
 * @Date: 2018/7/9 16:11
 * @Description: group对应的coordinator不在本机
 */
public class CoordinatorNotCorrectException extends ServerException implements RetriableException{

    public CoordinatorNotCorrectException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public CoordinatorNotCorrectException(String message) {
        super(message);
    }
}
