package com.vcvinci.common.record;

import com.vcvinci.common.record.AbstractRecordBatch.BatchRecords;
import com.vcvinci.common.util.CheckSum;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

/**
 * @author yankai.zhang
 * @since 2019/1/21 上午1:09
 */
public class DefaultBatchRecords implements BatchRecords {

    private static final int CHECK_SUM_SIZE = 2;

    private final ByteBuffer buffer;

    private final int count;

    public DefaultBatchRecords(ByteBuffer buffer, int count) {
        this.buffer = buffer;
        this.count = count;
    }

    public static ByteBuffer build(List<DefaultRecord> records) {
        int size = 0;
        for (DefaultRecord record : records) {
            size += record.sizeInBytes();
        }
        ByteBuffer buffer = ByteBuffer.allocate(size + CHECK_SUM_SIZE);
        ByteBuffer dup = buffer.duplicate();
        for (DefaultRecord record : records) {
            record.toStruct().writeTo(buffer);
        }
        dup.limit(buffer.position());
        CheckSum.putCRC16(buffer, CheckSum.crc16(dup));
        dup.limit(buffer.position());
        return dup;
    }

    @Override
    public boolean checkValid() {
        return (this.buffer.remaining() - (CHECK_SUM_SIZE)) / this.count > 0 && CheckSum.crc16(this.buffer) == CheckSum.OK16;
    }

    @Override
    public ByteBuffer exposeBuffer() {
        return this.buffer;
    }

    @Override
    public Iterator<Record> iterator() {
        final ByteBuffer dup = this.buffer.duplicate();
        return new Iterator<Record>() {

            private int n = 0;

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean hasNext() {
                return n < count;
            }

            @Override
            public Record next() {
                return DefaultRecord.readFrom(dup, n++);
            }
        };
    }
}
