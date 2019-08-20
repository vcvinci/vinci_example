package com.vcvinci.common.exception.common;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年6月11日 上午10:05:20
 * @description 类说明
 */
public class SystemException extends CommonException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

}
