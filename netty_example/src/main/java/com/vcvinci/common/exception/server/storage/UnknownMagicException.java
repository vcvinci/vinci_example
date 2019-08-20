package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;

/**
 * @author yankai.zhang
 * @since 2019/1/21 上午2:36
 */
public class UnknownMagicException extends DoveProcessibleException {
    public UnknownMagicException(byte magic) {
        super("unknown magic " + magic);
    }
}
