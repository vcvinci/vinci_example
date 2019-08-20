package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;

import java.io.IOException;

/**
 * @author yankai.zhang
 * @since 2019/1/22 下午9:31
 */
public class UnknownIOException extends DoveProcessibleException {
    public UnknownIOException(IOException e) {
        super(e);
    }
}
