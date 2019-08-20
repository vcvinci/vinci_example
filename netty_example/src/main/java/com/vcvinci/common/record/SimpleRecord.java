package com.vcvinci.common.record;

import com.vcvinci.common.exception.server.storage.InvalidBatchException;

import java.nio.ByteBuffer;

public class SimpleRecord {
    private final RecordBatch batch;

    private final Record record;

    private final int n;

    public SimpleRecord(RecordBatch batch, Record record, int n) {
        this.batch = batch;
        this.record = record;
        this.n = n;
    }

    public long storeTimestamp() throws InvalidBatchException {
        return this.batch.storeTimestamp();
    }

    public long clientTimestamp() throws InvalidBatchException {
        return this.batch.clientTimestamp();
    }

    public byte magic() throws InvalidBatchException {
        return this.batch.magic();
    }

    public int partitionLeaderEpoch() throws InvalidBatchException {
        return this.batch.partitionLeaderEpoch();
    }

    public boolean hasKey() {
        return this.record.hasKey();
    }

    public ByteBuffer key() {
        return this.record.key();
    }

    public boolean hasValue() {
        return this.record.hasValue();
    }

    public ByteBuffer value() {
        return this.record.value();
    }

    public long offset() throws InvalidBatchException {
        return this.batch.batchOffset() + n;
    }
}
