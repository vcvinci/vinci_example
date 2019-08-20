package com.vcvinci.common.exception.client;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @date 2018年6月27日 下午3:40:20
 * @description 类说明
 */

public class NonsupportReqRespMapperException extends ClientException {

    private static final long serialVersionUID = 1L;

    public NonsupportReqRespMapperException() {
    }

    public NonsupportReqRespMapperException(String message) {
        super(message);
    }

    public NonsupportReqRespMapperException(Throwable cause) {
        super(cause);
    }

    public NonsupportReqRespMapperException(String message, Throwable cause) {
        super(message, cause);
    }
}