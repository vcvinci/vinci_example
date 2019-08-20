package com.vcvinci.common.serialization;

import java.io.Closeable;
import java.util.Map;

public interface Deserializer<T> extends Closeable {

    /**
     * d
     * 配置这个类
     *
     * @param configs 配置的键/值对
     * @param isKey   是键还是值
     */
    void configure(Map<String, ?> configs, boolean isKey);

    /**
     * 将记录值从字节数组反序列化为值或对象。
     *
     * @param topic 与数据相关的主题
     * @param data  序列化的字节数;可能是零;建议通过返回值或null而不是抛出异常来处理null。
     * @return 反序列化类型的数据;可能是零
     */
    T deserialize(String topic, byte[] data);

    @Override
    void close();
}
