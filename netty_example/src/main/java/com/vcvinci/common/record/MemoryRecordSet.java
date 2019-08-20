package com.vcvinci.common.record;

import com.vcvinci.common.exception.server.storage.InvalidBatchException;

import java.nio.ByteBuffer;
import java.util.Iterator;

/**
 * 功能描述: 批量消息的集合
 * 
 * @author vinci
 * @date 2019-01-09 11:23
 */

public class MemoryRecordSet implements RecordSet {

    // FIXME: 可配置
    private static final int maxMessageSize = 1 * 1024 * 1024;

    private final ByteBuffer buffer;

    private final Iterable<MemoryRecordBatch> batches = new Iterable<MemoryRecordBatch>() {
        @Override
        public Iterator<MemoryRecordBatch> iterator() {
            final ByteBuffer dup = buffer.duplicate();
            return new RecordBatchIterator<MemoryRecordBatch>() {

                @Override
                protected MemoryRecordBatch tryMakeNext() throws InvalidBatchException {
                    if (!dup.hasRemaining()) {
                        return null;
                    }
                    MemoryRecordBatch batch = new MemoryRecordBatch(dup);
                    if (dup.remaining() < batch.getHeader().sizeInBytes()) {
                        return null;
                    }
                    return batch;
                }

                @Override
                protected void advance(int size) {
                    dup.position(dup.position() + size);
                }
            };
        }
    };

    public MemoryRecordSet(ByteBuffer buffer) {
        this.buffer = buffer;
    }


    public MemoryRecordSet(MemoryRecordBatch... batches) throws InvalidBatchException {
        // TODO: 避免多余的内存拷贝
        int size = 0;
        for (MemoryRecordBatch batch : batches) {
            size += batch.sizeInBytes();
        }
        ByteBuffer buffer = ByteBuffer.allocate(size);
        for (MemoryRecordBatch batch : batches) {
            batch.writeTo(buffer);
        }
        this.buffer = buffer;
        this.buffer.flip();
    }

    @Override
    public Iterable<MemoryRecordBatch> batches() {
        return batches;

    }

    @Override
    public int sizeInBytes() {
        return buffer.remaining();
    }

    public ByteBuffer getBuffer() {
        return this.buffer;
    }

}
