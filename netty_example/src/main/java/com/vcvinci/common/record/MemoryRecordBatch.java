package com.vcvinci.common.record;

import com.vcvinci.common.exception.server.storage.InvalidBatchException;
import com.vcvinci.common.exception.server.storage.LoadDataException;
import com.vcvinci.common.exception.server.storage.UnknownMagicException;
import com.vcvinci.common.util.CheckSum;
import org.apache.commons.io.EndianUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

/**
 * 结构如下：
 *
 * <pre>
 * RecordBatch =>
 *  -----------------------------------
 *  Magic => Int8
 *  Length => Int32 Batch总字节数
 *  CRC8  => Int8
 *  -----------------------------------
 *  BatchOffset => Int64
 *  storeTimestamp => Int64
 *  PartitionLeaderEpoch => Int32
 *  clientTimestamp => Int64 1970 epoch TODO: 重置为发布日期
 *  BatchSequence => Int32
 *  RecordCount  => Int32
 *  CRC16  =>  Int16
 *  -----------------------------------
 *  Records => [Record]
 *  CRC16  =>  Int16
 * </pre>
 */
public class MemoryRecordBatch extends AbstractRecordBatch implements RecordBatch, MutableRecordBatch {

    private final ByteBuffer buffer;

    private MutableBatchDetail detail;

    private BatchRecords records;

    public MemoryRecordBatch(ByteBuffer buffer) {
        this.buffer = buffer.duplicate();
    }

    public MemoryRecordBatch(byte magic, long clientTimestamp, int baseSequence, List<DefaultRecord> records)
            throws UnknownMagicException {
        if (magic == 0) {
            ByteBuffer recordsBuffer = DefaultBatchRecords.build(records);
            int sizeInBytes = DefaultBatchHeader.TOTAL_SIZE + MutableBatchDetail.TOTAL_SIZE + recordsBuffer.remaining();
            this.buffer = ByteBuffer.allocate(sizeInBytes);
            DefaultBatchHeader.writeTo(this.buffer, magic, sizeInBytes);
            MutableBatchDetail.writeTo(this.buffer, clientTimestamp, baseSequence, records.size());
            this.buffer.put(recordsBuffer);
            this.buffer.flip();
        } else {
            throw new UnknownMagicException(magic);
        }
    }


    @Override
    public void setBaseOffset(long offset) throws InvalidBatchException {
        this.getDetail().setBaseOffset(offset);
    }

    @Override
    public void setStoreTimestamp(long timestamp) throws InvalidBatchException {
        this.getDetail().setStoreTimestamp(timestamp);
    }

    @Override
    public void setPartitionLeaderEpoch(int epoch) throws InvalidBatchException {
        this.getDetail().setPartitionLeaderEpoch(epoch);
    }

    @Override
    public void updateCheckSum() throws InvalidBatchException {
        this.getDetail().updateCheckSum();
    }

    @Override
    public MutableBatchDetail getDetail() throws InvalidBatchException {
        if (this.detail != null) {
            return this.detail;
        }
        BatchHeader header = this.getHeader();
        if (header.magic() == 0) {
            try {
                MutableBatchDetail detail = new MutableBatchDetail(this.consumeNext(MutableBatchDetail.TOTAL_SIZE));
                if (!detail.checkValid()) {
                    throw new InvalidBatchException();
                }
                return this.detail = detail;
            } catch (LoadDataException e) {
                throw new InvalidBatchException(e);
            }
        } else {
            throw new InvalidBatchException(new UnknownMagicException(header.magic()));
        }
    }

    @Override
    public BatchRecords getRecords() throws InvalidBatchException {
        if (this.records != null) {
            return this.records;
        }
        BatchHeader header = this.getHeader();
        MutableBatchDetail detail = this.getDetail();
        if (header.magic() == 0) {
            try {
                DefaultBatchRecords records = new DefaultBatchRecords(
                        this.consumeNext(
                                header.sizeInBytes() - DefaultBatchHeader.TOTAL_SIZE - DefaultBatchDetail.TOTAL_SIZE),
                        detail.count());
                if (!records.checkValid()) {
                    throw new InvalidBatchException();
                }
                return this.records = records;
            } catch (LoadDataException e) {
                throw new InvalidBatchException(e);
            }
        } else {
            throw new InvalidBatchException(new UnknownMagicException(header.magic()));
        }
    }

    @Override
    protected ByteBuffer consumeNext(int size) throws LoadDataException {
        if (this.buffer.remaining() < size) {
            throw new LoadDataException(this.buffer.remaining(), size);
        }
        ByteBuffer slice = this.buffer.slice();

        this.buffer.position(this.buffer.position() + size);
        slice.limit(size);
        return slice;
    }

    /**
     * @see MutableRecordBatch
     */
    static class MutableBatchDetail extends DefaultBatchDetail {

        public MutableBatchDetail(ByteBuffer buffer) {
            super(buffer);
        }

        void setBaseOffset(long offset) {
            this.buffer.putLong(BATCH_OFFSET_OFFSET, offset);
        }

        void setStoreTimestamp(long timestamp) {
            this.buffer.putLong(STORE_TIMESTAMP_OFFSET, timestamp);
        }

        void setPartitionLeaderEpoch(int epoch) {
            this.buffer.putInt(PARTITION_LEADER_EPOCH_OFFSET, epoch);
        }

        void updateCheckSum() {
            ByteBuffer dup = this.buffer.duplicate();
            dup.limit(CHECK_SUM_OFFSET);
            this.buffer.putShort(CHECK_SUM_OFFSET, ByteOrder.LITTLE_ENDIAN == buffer.order() ? CheckSum.crc16(dup)
                    : EndianUtils.swapShort(CheckSum.crc16(dup)));
        }
    }

}
