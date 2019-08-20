package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;

/**
 * @author yankai.zhang
 * @since 2019/1/2 下午4:18
 */
public class SliceRangeException extends DoveProcessibleException {
	public SliceRangeException(int sStart, int sEnd, int dStart, int dEnd) {
		super("slice range [" + sStart + "," + sEnd + ") not in data range [" + dStart + "," + dEnd + ")");
	}
}
