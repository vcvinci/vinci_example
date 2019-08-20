package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;

/**
 * @author yankai.zhang
 * @since 2019/2/18 上午10:35
 */
public class UnknownStorageException extends DoveProcessibleException {
    public UnknownStorageException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
