package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;

/**
 * @author yankai.zhang
 * @since 2019/1/9 下午2:24
 */
public class IndexOrderException extends DoveProcessibleException {
	public IndexOrderException(long lastOffset, long lastTimestamp, int lastPosition, long offset, long timestamp,
			int position) {
		super("new index entry[relativeOffset=" + offset + ",timestamp=" + timestamp + ",position=" + position
				+ "] must strictly increasing compare to last index entry[relativeOffset=" + lastOffset + ",timestamp="
				+ lastTimestamp + ",position=" + lastPosition + "]");
	}
}
