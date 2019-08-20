package com.vcvinci.common.record;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月3日 上午8:44:27
 * @description 类说明
 */
public interface RecordSet {

    /**
     * 获取记录批次。请注意，签名允许子类*返回更具体的批处理类型。 这样可以实现优化，例如就地偏移*赋值（参见例如{@link MemoryRecordBatch}）
     * 
     * @return 记录批次日志上的迭代器
     */
    Iterable<? extends RecordBatch> batches();




    /**
     *
     * @return 消息集合总大小，保证大于等于所有batch之和，但不保证相等
     */
    int sizeInBytes();

}
