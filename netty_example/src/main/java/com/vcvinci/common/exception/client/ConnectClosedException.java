package com.vcvinci.common.exception.client;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @date 2018年6月27日 下午3:40:20
 * @description 类说明
 */

public class ConnectClosedException extends ClientException {

    private static final long serialVersionUID = 1L;

    public ConnectClosedException() {
    }

    public ConnectClosedException(String message) {
        super(message);
    }

    public ConnectClosedException(Throwable cause) {
        super(cause);
    }

    public ConnectClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}