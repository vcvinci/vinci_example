package com.vcvinci.common.exception.common;

import com.vcvinci.common.exception.DoveException;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年6月9日 下午5:25:09
 * @description 类说明
 */
public class CommonException extends DoveException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(Throwable cause) {
        super(cause);
    }

    public CommonException() {
        super();
    }

}
