package com.vcvinci.common.exception.client;

/**
 * An error occurred on the server for which the client doesn't have a corresponding error code. This is generally an
 * unexpected error.
 */

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @date 2018年6月27日 下午3:39:12
 * @description An error occurred on the server for which the client doesn't have a corresponding error code. This is generally an
 * unexpected error.
 */
public class UnknownClientException extends ClientException {

    private static final long serialVersionUID = 1L;

    public UnknownClientException() {
    }

    public UnknownClientException(String message) {
        super(message);
    }

    public UnknownClientException(Throwable cause) {
        super(cause);
    }

    public UnknownClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
