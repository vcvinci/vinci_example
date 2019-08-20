package com.vcvinci.common.exception.api;

import com.vcvinci.common.exception.DoveException;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年6月10日 下午2:29:10
 * @description 类说明
 */
public class ApiException extends DoveException {

    private static final long serialVersionUID = 1L;

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException() {
        super();
    }

    /* avoid the expensive and useless stack trace for api exceptions */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}