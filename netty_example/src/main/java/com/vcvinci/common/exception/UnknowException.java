package com.vcvinci.common.exception;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年6月11日 下午1:39:42
 * @description 类说明
 */
public class UnknowException extends DoveException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public UnknowException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknowException(String message) {
        super(message);
    }

    public UnknowException(Throwable cause) {
        super(cause);
    }

    public UnknowException() {
        super();
    }

}
