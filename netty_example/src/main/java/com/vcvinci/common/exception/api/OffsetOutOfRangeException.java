package com.vcvinci.common.exception.api;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version v1
 * @date 2018年7月9日 下午4:24:31
 * @description TODO
 */
public class OffsetOutOfRangeException extends ApiException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public OffsetOutOfRangeException() {
    }

    public OffsetOutOfRangeException(String message) {
        super(message);
    }

    public OffsetOutOfRangeException(Throwable cause) {
        super(cause);
    }

    public OffsetOutOfRangeException(String message, Throwable cause) {
        super(message, cause);
    }
}