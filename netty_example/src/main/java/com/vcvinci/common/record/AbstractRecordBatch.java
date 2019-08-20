package com.vcvinci.common.record;

import com.vcvinci.common.exception.ThrowUtils;
import com.vcvinci.common.exception.server.storage.InvalidBatchException;
import com.vcvinci.common.exception.server.storage.LoadDataException;

import java.nio.ByteBuffer;
import java.util.Iterator;

/**
 * 版本、介质无关的RecordBatch框架
 *
 * @author yankai.zhang
 * @since 2019/1/18 下午5:56
 */
public abstract class AbstractRecordBatch implements RecordBatch {

    private BatchHeader header;

    @Override
    public byte magic() throws InvalidBatchException {
        return this.getHeader().magic();
    }

    @Override
    public int sizeInBytes() throws InvalidBatchException {
        return this.getHeader().sizeInBytes();
    }

    @Override
    public boolean checkValid() {
        try {
            this.ensureValid();
            return true;
        } catch (InvalidBatchException ignored) {
            return false;
        }
    }

    @Override
    public long batchOffset() throws InvalidBatchException {
        return this.getDetail().batchOffset();
    }

    @Override
    public long storeTimestamp() throws InvalidBatchException {
        return this.getDetail().storeTimestamp();
    }

    @Override
    public long clientTimestamp() throws InvalidBatchException {
        return this.getDetail().clientTimestamp();
    }

    @Override
    public int batchSequence() throws InvalidBatchException {
        return this.getDetail().batchSequence();
    }

    @Override
    public int count() throws InvalidBatchException {
        return this.getDetail().count();
    }

    @Override
    public Iterator<Record> iterator() {
        try {
            return this.getRecords().iterator();
        } catch (InvalidBatchException e) {
            // TODO: checked
            throw ThrowUtils.eraseChecked(e);
        }
    }

    @Override
    public int partitionLeaderEpoch() throws InvalidBatchException {
        return this.getDetail().partitionLeaderEpoch();
    }

    /**
     *
     * @return 校验无误的header
     * @throws InvalidBatchException
     */
    public BatchHeader getHeader() throws InvalidBatchException {
        if (this.header != null) {
            return this.header;
        }
        try {
            DefaultBatchHeader header = new DefaultBatchHeader(this.consumeNext(DefaultBatchHeader.TOTAL_SIZE));
            if (!header.checkValid()) {
                throw new InvalidBatchException();
            }
            return this.header = header;
        } catch (LoadDataException e) {
            throw new InvalidBatchException(e);
        }
    }

    /**
     *
     * @return 校验无误的detail
     * @throws InvalidBatchException
     */
    public abstract BatchDetail getDetail() throws InvalidBatchException;

    /**
     *
     * @return 校验无误的records
     * @throws InvalidBatchException
     */
    public abstract BatchRecords getRecords() throws InvalidBatchException;

    @Override
    public void writeTo(ByteBuffer buffer) throws InvalidBatchException {
        buffer.put(this.getHeader().exposeBuffer().duplicate());
        buffer.put(this.getDetail().exposeBuffer().duplicate());
        buffer.put(this.getRecords().exposeBuffer().duplicate());
    }

    /**
     * 从连续介质（byte buffer、channel等）的当前位置消费一段数据
     * 
     * @param size
     *            数据长度
     * @return 数据
     * @throws LoadDataException
     *             加载数据失败
     */
    protected abstract ByteBuffer consumeNext(int size) throws LoadDataException;

    @Override
    public void ensureValid() throws InvalidBatchException {
        // TODO: 更详细的异常信息
        this.getHeader();
        this.getDetail();
        this.getRecords();
    }

    @Override
    public long nextBatchOffset() throws InvalidBatchException {
        return this.batchOffset() + this.count();
    }

    public interface BatchHeader {
        byte magic();

        int sizeInBytes();

        boolean checkValid();

        ByteBuffer exposeBuffer();
    }

    public interface BatchDetail {
        long batchOffset();

        long storeTimestamp();

        int partitionLeaderEpoch();

        long clientTimestamp();

        int batchSequence();

        int count();

        boolean checkValid();

        ByteBuffer exposeBuffer();
    }

    public interface BatchRecords extends Iterable<Record> {
        boolean checkValid();

        ByteBuffer exposeBuffer();
    }

}
