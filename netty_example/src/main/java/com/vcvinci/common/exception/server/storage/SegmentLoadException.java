package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;

import java.io.File;

/**
 * @author yankai.zhang
 * @since 2019/1/16 下午3:07
 */
public class SegmentLoadException extends DoveProcessibleException {
    public SegmentLoadException(File partitionDir) {
        super("failed to load segments from dir " + partitionDir);
    }
}
