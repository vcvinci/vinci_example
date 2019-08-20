package com.vcvinci.common.record;

import com.vcvinci.common.record.AbstractRecordBatch.BatchDetail;
import com.vcvinci.common.util.CheckSum;

import java.nio.ByteBuffer;

/**
 * @author yankai.zhang
 * @since 2019/1/21 上午1:06
 */
public class DefaultBatchDetail implements BatchDetail {

    protected static final int BATCH_OFFSET_OFFSET = 0;

    protected static final int BACH_OFFSET_SIZE = 8;

    protected static final int STORE_TIMESTAMP_OFFSET = BATCH_OFFSET_OFFSET + BACH_OFFSET_SIZE;

    protected static final int STORE_TIMESTAMP_SIZE = 8;

    protected static final int PARTITION_LEADER_EPOCH_OFFSET = STORE_TIMESTAMP_OFFSET + STORE_TIMESTAMP_SIZE;

    protected static final int PARTITION_LEADER_EPOCH_SIZE = 4;

    private static final int CLIENT_TIMESTAMP_OFFSET = PARTITION_LEADER_EPOCH_OFFSET + PARTITION_LEADER_EPOCH_SIZE;

    private static final int CLIENT_TIMESTAMP_SIZE = 8;

    private static final int BATCH_SEQUENCE_OFFSET = CLIENT_TIMESTAMP_OFFSET + CLIENT_TIMESTAMP_SIZE;

    private static final int BATCH_SEQUENCE_SIZE = 4;

    private static final int COUNT_OFFSET = BATCH_SEQUENCE_OFFSET + BATCH_SEQUENCE_SIZE;

    private static final int COUNT_SIZE = 4;

    protected static final int CHECK_SUM_OFFSET = COUNT_OFFSET + COUNT_SIZE;

    protected static final int CHECK_SUM_SIZE = 2;

    public static final int TOTAL_SIZE = CHECK_SUM_OFFSET + CHECK_SUM_SIZE;

    protected final ByteBuffer buffer;

    public DefaultBatchDetail(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public static ByteBuffer writeTo(ByteBuffer buffer, long clientTimestamp, int batchSequence, int count) {
        ByteBuffer dup = buffer.duplicate();
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putInt(0);
        buffer.putLong(clientTimestamp);
        buffer.putInt(batchSequence);
        buffer.putInt(count);
        dup.limit(buffer.position());
        CheckSum.putCRC16(buffer, CheckSum.crc16(dup));
        dup.limit(buffer.position());
        return dup;
    }

    @Override
    public long batchOffset() {
        return this.buffer.getLong(BATCH_OFFSET_OFFSET);
    }

    @Override
    public long storeTimestamp() {
        return this.buffer.getLong(STORE_TIMESTAMP_OFFSET);
    }

    @Override
    public int partitionLeaderEpoch() {
        return this.buffer.getInt(PARTITION_LEADER_EPOCH_OFFSET);
    }

    @Override
    public long clientTimestamp() {
        return this.buffer.getLong(CLIENT_TIMESTAMP_OFFSET);
    }

    @Override
    public int batchSequence() {
        return this.buffer.getInt(BATCH_SEQUENCE_OFFSET);
    }

    @Override
    public int count() {
        return this.buffer.getInt(COUNT_OFFSET);
    }

    @Override
    public boolean checkValid() {
        return this.buffer.remaining() == TOTAL_SIZE && CheckSum.crc16(this.buffer) == CheckSum.OK16;
    }

    @Override
    public ByteBuffer exposeBuffer() {
        return this.buffer;
    }
}
