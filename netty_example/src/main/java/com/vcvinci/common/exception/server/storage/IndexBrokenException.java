package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;

/**
 * 索引文件已经损坏，无法跟数据文件匹配
 *
 * @author yankai.zhang
 * @since 2019/1/2 下午4:23
 */
public class IndexBrokenException extends DoveProcessibleException {
	public IndexBrokenException(long offset, int position, SliceRangeException e) {
		super("index entry [offset=" + offset + ", position=" + position + "] is invalid", e);
	}

	public IndexBrokenException(int position, long indexOffset, long dataOffset) {
		super("offset of message at position " + position + " from index offset " + indexOffset + " is actually "
				+ dataOffset);
	}

	public IndexBrokenException(int position, long indexOffset) {
		super("no message found at position " + position + " from index offset " + indexOffset);
	}
}
