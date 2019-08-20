package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;

/**
 * @author yankai.zhang
 * @since 2019/1/19 下午2:41
 */
public class InvalidBatchException extends DoveProcessibleException {
    public InvalidBatchException(Throwable cause) {
        super(cause);
    }

    public InvalidBatchException() {

    }
}
