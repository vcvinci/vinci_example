package com.vcvinci.common.exception.server;

import com.vcvinci.common.exception.RetriableException;

/**
 * @Auther: yuxin.chen01
 * @Date: 2018/7/5 16:31
 * @Description: 协调者不可用, 该异常发生在协调者leader宕机
 */
public class CoordinatorNotAvailiableException extends ServerException implements RetriableException{

    public CoordinatorNotAvailiableException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public CoordinatorNotAvailiableException(String message) {
        super(message);
    }
}
