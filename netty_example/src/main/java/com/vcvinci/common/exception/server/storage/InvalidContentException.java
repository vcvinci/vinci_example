package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;

import java.io.File;

/**
 * @author yankai.zhang
 * @since 2019/2/14 上午11:12
 */
public class InvalidContentException extends DoveProcessibleException {
    public InvalidContentException(File file, Throwable cause) {
        super("content of file " + file + " is invalid", cause);
    }

    public InvalidContentException(File file, String s) {
        super("content of file " + file + " is invalid: " + s);
    }
}
