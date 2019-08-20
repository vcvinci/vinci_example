package com.vcvinci.common.exception.server.storage;

import com.vcvinci.common.exception.DoveProcessibleException;

/**
 * @author yankai.zhang
 * @since 2019/1/8 上午10:05
 */
public class InvalidOffsetException extends DoveProcessibleException {
    private InvalidOffsetException(String msg) {
        super(msg);
    }

    /**
     * offset值是负数
     * 
     * @param offset
     * @return
     */
    public static InvalidOffsetException negativeOffset(long offset) {
        return new InvalidOffsetException("negative offset " + offset);
    }

    /**
     * 位于<code>position</code>处的消息，实际offset是<code>dataOffset</code>而不是<code>offset</code>
     * 
     * @param position
     * @param offset
     * @param dataOffset
     * @return
     */
    public static InvalidOffsetException offsetNotMatchPosition(int position, long offset, long dataOffset) {
        return new InvalidOffsetException(
                "invalid offset " + offset + " at position " + position + ", actual offset is " + dataOffset);
    }

    /**
     * 在<code>position</code>处不存在消息数据，所以对应的<code>offset</code>是错误的
     * 
     * @param position
     * @param offset
     * @return
     */
    public static InvalidOffsetException noDataAtPosition(int position, long offset) {
        return new InvalidOffsetException(
                "invalid offset " + offset + " at position " + position + ", no message found");
    }

    /**
     * position超过当前限制
     * 
     * @param position
     * @param maxPosition
     * @return
     */
    public static InvalidOffsetException positionTooLarge(int position, int maxPosition) {
        return new InvalidOffsetException("invalid position " + position + " larger than max position " + maxPosition);
    }

    /**
     * endOffset的base offset比startOffset的base offset还小
     * 
     * @param endBaseOffset
     * @param endOffset
     * @param startBaseOffset
     * @param startOffset
     * @return
     */
    public static InvalidOffsetException baseEndBeforeStart(long endBaseOffset, long endOffset, long startBaseOffset,
            long startOffset) {
        return new InvalidOffsetException("base offset of start offset " + startOffset + " is " + startBaseOffset
                + ", so base  offset of end offset " + endOffset + " can't be " + endBaseOffset);
    }

    /**
     * offset比base offset还小
     * 
     * @param offset
     * @param baseOffset
     * @return
     */
    public static InvalidOffsetException offsetBeforeBase(long offset, long baseOffset) {
        return new InvalidOffsetException("offset " + offset + " must greater than base offset " + baseOffset);
    }

    /**
     * 消息中的offset与当前存储的下一条offset不一致
     * 
     * @param batchOffset
     * @param nextOffset
     * @return
     */
    public static InvalidOffsetException offsetNotMatch(long batchOffset, long nextOffset) {
        return new InvalidOffsetException(
                "offset " + batchOffset + " in record batch not match next offset " + nextOffset + " of local replica");
    }

    /**
     * offset不是batch的边界
     * 
     * @param offset
     * @param batchOffset
     * @return
     */
    public static InvalidOffsetException notBatchBoundary(long offset, long batchOffset) {
        return new InvalidOffsetException(
                "offset " + offset + " is not a batch boundary, it's inside batch at " + batchOffset);
    }
}
