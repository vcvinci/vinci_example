package com.vcvinci.common.exception.client;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @date 2018年6月27日 下午3:40:20
 * @description 类说明
 */

public class NoAvailablePartitionException extends ClientException {

    private static final long serialVersionUID = 1L;

    public NoAvailablePartitionException() {
    }

    public NoAvailablePartitionException(String message) {
        super(message);
    }

    public NoAvailablePartitionException(Throwable cause) {
        super(cause);
    }

    public NoAvailablePartitionException(String message, Throwable cause) {
        super(message, cause);
    }
}