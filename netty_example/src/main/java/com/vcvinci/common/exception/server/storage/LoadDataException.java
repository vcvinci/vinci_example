package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;

/**
 * @author yankai.zhang
 * @since 2019/1/19 下午5:25
 */
public class LoadDataException extends DoveProcessibleException {
    public LoadDataException(Throwable t) {
        super(t);
    }

    public LoadDataException(int actual, int expect) {
        super("expect to load "  + expect + " bytes, but " + actual + " actually");
    }
}
