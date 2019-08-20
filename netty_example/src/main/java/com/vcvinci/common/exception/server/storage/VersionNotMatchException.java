package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;

/**
 * @author yankai.zhang
 * @since 2019/2/14 上午10:53
 */
public class VersionNotMatchException extends DoveProcessibleException {
    public VersionNotMatchException(int expect, int actual) {
        super("expect version " + expect + ", but actual " + actual);
    }
}
