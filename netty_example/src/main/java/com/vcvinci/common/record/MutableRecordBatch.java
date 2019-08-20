package com.vcvinci.common.record;

import com.vcvinci.common.exception.server.storage.InvalidBatchException;

/**
 * @author yankai.zhang
 * @since 2018/12/26 上午2:42
 */
public interface MutableRecordBatch extends RecordBatch {
    /**
     * batch第一条record的offset
     */
    void setBaseOffset(long offset) throws InvalidBatchException;

    /**
     * 服务端存储时间
     */
    void setStoreTimestamp(long timestamp) throws InvalidBatchException;

    /**
     * 服务端存储时的分区leader epoch，用于解决副本同步冲突问题
     */
    void setPartitionLeaderEpoch(int epoch) throws InvalidBatchException;

    void updateCheckSum() throws InvalidBatchException;
}
