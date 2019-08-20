package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;

import java.nio.file.DirectoryNotEmptyException;

/**
 * @author yankai.zhang
 * @since 2019/2/12 下午3:12
 */
public class NotRegularException extends DoveProcessibleException {
    public NotRegularException(String absolutePath) {
        super(absolutePath + " is not an regular file");
    }

    public NotRegularException(String absolutePath, DirectoryNotEmptyException cause) {
        super(absolutePath + " is not an regular file", cause);
    }
}
