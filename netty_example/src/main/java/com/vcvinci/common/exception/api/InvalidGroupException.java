package com.vcvinci.common.exception.api;

/**
 * @Auther: yuxin.chen01
 * @Date: 2018/7/5 17:06
 * @Description:group参数无效时,抛出的异常
 */
public class InvalidGroupException extends ApiException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public InvalidGroupException() {
    }

    public InvalidGroupException(String message) {
        super(message);
    }

    public InvalidGroupException(Throwable cause) {
        super(cause);
    }

    public InvalidGroupException(String message, Throwable cause) {
        super(message, cause);
    }
}
