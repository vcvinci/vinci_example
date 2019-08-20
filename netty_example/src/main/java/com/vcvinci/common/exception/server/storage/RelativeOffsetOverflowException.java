package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;

/**
 *
 * 索引文件中的相对offset超出int的范围
 *
 * @author yankai.zhang
 * @since 2019/1/10 下午9:52
 */
public class RelativeOffsetOverflowException extends DoveProcessibleException {
	public RelativeOffsetOverflowException(long offset, int minValue, int maxValue) {
		super("relative offset " + offset + " must in integer range [" + minValue + ", " + maxValue + "]");
	}
}
