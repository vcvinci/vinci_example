package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;

import java.io.File;
import java.io.IOException;

/**
 * @author yankai.zhang
 * @since 2018/12/27 上午8:09
 */
public class FailedCreateDirException extends DoveProcessibleException {
    public FailedCreateDirException(File dir, IOException e) {
        super("failed to create dir " + dir.getAbsolutePath(), e);
    }
}
