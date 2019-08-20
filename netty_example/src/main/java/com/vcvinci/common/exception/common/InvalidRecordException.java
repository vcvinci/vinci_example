package com.vcvinci.common.exception.common;

/**
 * 无效日志记录异常
 * @author vinci
 * @date 2018-12-17 11:55
 */
public class InvalidRecordException extends CommonException {

    private static final long serialVersionUID = 1L;

    public InvalidRecordException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRecordException(String message) {
        super(message);
    }
}
