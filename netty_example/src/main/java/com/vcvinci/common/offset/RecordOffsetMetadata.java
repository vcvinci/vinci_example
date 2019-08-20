package com.vcvinci.common.offset;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月3日 上午8:56:49
 * @description 类说明
 */
public class RecordOffsetMetadata {

    public static final long UNKNOWN_OFFSET = -1;

    public static final RecordOffsetMetadata UNKNOWN_METADATA = new RecordOffsetMetadata(UNKNOWN_OFFSET, UNKNOWN_OFFSET, -1);


    private long baseOffset;
    private long offset;
    private int position;

    public RecordOffsetMetadata() {
    }

    /**
     *
     * @param baseOffset 文件分段的起始offset
     * @param offset 消息绝对offset
     * @param position 消息在文件分段中的字节位置，单个文件不能超过2GB
     */
    public RecordOffsetMetadata(long baseOffset, long offset, int position) {
        this.baseOffset = baseOffset;
        this.offset = offset;
        this.position = position;
    }

    public long getBaseOffset() {
        return baseOffset;
    }

    public long getOffset() {
        return offset;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "baseOffset="+ baseOffset + ", offset=" + offset + ", position="+position;
    }
}
