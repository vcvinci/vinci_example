package com.vcvinci.common.exception.client;

/**
 * @Auther: yuxin.chen01
 * @Date: 2018/12/20 10:57
 * @Description:网络层初始化等抛出自定义异常
 */
public class NetworkException extends ClientException {

    private static final long serialVersionUID = 1L;

    public NetworkException() {
    }

    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(Throwable cause) {
        super(cause);
    }

    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
