package com.vcvinci.common.exception.client;

import com.vcvinci.common.exception.RetriableException;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @date 2018年6月27日 下午3:40:20
 * @description 当没有有效broker连接地址时抛出的异常
 */
public class NoAvaliableHostException extends ClientException implements RetriableException {

    private static final long serialVersionUID = 1L;

    public NoAvaliableHostException() {
        super();
    }

    public NoAvaliableHostException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAvaliableHostException(String message) {
        super(message);
    }

    public NoAvaliableHostException(Throwable cause) {
        super(cause);
    }
}
