package com.vcvinci.common.record;

import com.vcvinci.common.record.AbstractRecordBatch.BatchHeader;
import com.vcvinci.common.util.CheckSum;

import java.nio.ByteBuffer;

/**
 * @author yankai.zhang
 * @since 2019/1/21 上午1:04
 */
public class DefaultBatchHeader implements BatchHeader {

    private static final int MAGIC_OFFSET = 0;

    private static final int MAGIC_SIZE = 1;

    private static final int SIZE_OFFSET = MAGIC_OFFSET + MAGIC_SIZE;

    private static final int SIZE_SIZE = 4;

    private static final int CHECK_SUM_OFFSET = SIZE_OFFSET + SIZE_SIZE;

    private static final int CHECK_SUM_SIZE = 1;

    public static final int TOTAL_SIZE = CHECK_SUM_OFFSET + CHECK_SUM_SIZE ;

    final ByteBuffer buffer;

    public DefaultBatchHeader(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public static ByteBuffer writeTo(ByteBuffer buffer, byte magic, int size) {
        ByteBuffer dup = buffer.duplicate();
        buffer.put(magic);
        buffer.putInt(size);
        dup.limit(buffer.position());
        buffer.put(CheckSum.crc8(dup));
        dup.limit(buffer.position());
        return dup;
    }

    @Override
    public byte magic() {
        return buffer.get(MAGIC_OFFSET);
    }

    @Override
    public int sizeInBytes() {
        return buffer.getInt(SIZE_OFFSET);
    }

    @Override
    public boolean checkValid() {
        return buffer.remaining() == TOTAL_SIZE && CheckSum.crc8(buffer) == CheckSum.OK8;
    }

    @Override
    public ByteBuffer exposeBuffer() {
        return this.buffer;
    }
}
