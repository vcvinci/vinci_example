package com.vcvinci.common.exception.common;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年6月11日 下午2:53:47
 * @description 类说明
 */
public class ErrorStateException extends CommonException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ErrorStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorStateException(String message) {
        super(message);
    }

}