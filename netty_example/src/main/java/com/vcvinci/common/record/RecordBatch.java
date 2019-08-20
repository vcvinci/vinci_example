package com.vcvinci.common.record;

import com.vcvinci.common.exception.server.storage.InvalidBatchException;

import java.nio.ByteBuffer;

/**
 * 记录批次是记录的容器，通常包含许多记录。
 */
public interface RecordBatch extends Iterable<Record> {

    /**
     * 检查数据的正确性，包括但不限于校验和。注意此方法可能会加载整个batch的所有数据。
     *
     * @return 是否合法
     */
    boolean checkValid();

    /**
     * 如果校验失败，则引发异常
     */
    void ensureValid() throws InvalidBatchException;

    /**
     * @return 消息存储时间
     */
    long storeTimestamp() throws InvalidBatchException;

    /**
     * @return 生产者发送时间
     */
    long clientTimestamp() throws InvalidBatchException;

    /**
     * 获取此记录批次中包含的基本偏移量。
     * 
     * @return 原始记录批次的第一个偏移量
     */
    long batchOffset() throws InvalidBatchException;

    /**
     * 获取此记录批次后的偏移量（即此批次中包含的最后一个偏移量加1）
     *
     * @return 此批次之后的下一个连续偏移
     */
    long nextBatchOffset() throws InvalidBatchException;

    /**
     * 获取此记录批次的记录格式版本（即其魔术值）
     *
     * @return Record 版本
     */
    byte magic() throws InvalidBatchException;

    int batchSequence() throws InvalidBatchException;

    /**
     * 获取此批处理的大小（以字节为单位），包括记录大小和批处理开销。
     * 
     * @return 此批次的大小（以字节为单位）
     */
    int sizeInBytes() throws InvalidBatchException;

    /**
     * @return 批处理中的记录数
     */
    int count() throws InvalidBatchException;

    /**
     * 将此记录批次写入缓冲区.
     * 
     * @param buffer
     *            要将批处理写入的缓冲区
     */
    void writeTo(ByteBuffer buffer) throws InvalidBatchException;

    /**
     * 获取此记录批次的分区领导者纪元
     * 
     * @return 领导者纪元或-1如果未知
     */
    int partitionLeaderEpoch() throws InvalidBatchException;

}
