package com.vcvinci.common.exception;

/**
 * 受检异常基类
 *
 * @author yankai.zhang
 * @since 2018/12/19 下午2:50
 */
public class DoveProcessibleException extends Exception {

    public DoveProcessibleException() {}

    public DoveProcessibleException(String msg) {
        super(msg);
    }

    public DoveProcessibleException(Throwable cause) {
        super(cause);
    }

    public DoveProcessibleException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
