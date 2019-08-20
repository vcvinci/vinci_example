package com.vcvinci.common.exception;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @date 2018年6月26日 下午5:21:41
 * @description 类说明
 */
public class DoveException extends RuntimeException {

    private final static long serialVersionUID = 1L;

    public DoveException(String message, Throwable cause) {
        super(message, cause);
    }

    public DoveException(String message) {
        super(message);
    }

    public DoveException(Throwable cause) {
        super(cause);
    }

    public DoveException() {
        super();
    }

}
