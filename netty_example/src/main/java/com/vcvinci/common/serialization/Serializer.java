package com.vcvinci.common.serialization;

import java.io.Closeable;
import java.util.Map;

public interface Serializer<T> extends Closeable {

    /**
     * 配置这个类
     *
     * @param configs 配置的键/值对
     * @param isKey   是键还是值
     */
    void configure(Map<String, ?> configs, boolean isKey);

    /**
     * 将{@code data}转换为字节数组。
     *
     * @param topic 主题相关的数据
     * @param data  被指定类型的数据
     * @return 序列化的字节
     */
    byte[] serialize(String topic, T data);

    /**
     * 关闭这个序列化器
     * <p>
     * 这种方法必须具有幂等性，因为它可以被多次调用。
     */
    @Override
    void close();
}
