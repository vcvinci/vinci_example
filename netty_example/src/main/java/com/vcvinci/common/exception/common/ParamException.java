package com.vcvinci.common.exception.common;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年6月11日 下午2:26:13
 * @description 类说明
 */
public class ParamException extends CommonException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamException(String message) {
        super(message);
    }

}
 