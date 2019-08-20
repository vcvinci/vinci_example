package com.vcvinci.common.record;

import com.vcvinci.common.Structable;

import java.nio.ByteBuffer;

/**

 * @author vinci
 * @date 2018-12-16 21:37
 */
public interface Record extends Structable {

    /**
     * 获取消息的字节大小
     * @return 字节大小
     */
    int sizeInBytes();

    /**
     * 获取密钥的大小（以字节为单位）
     * @return 键的大小，如果没有键，则返回-1
     */
    int keySize();

    /**
     * 检查此记录是否有密钥
     * @return 如果有密钥则为true，否则为false
     */
    boolean hasKey();

    /**
     * 获取记录的密钥
     * @return 键，如果没有，则返回null
     */
    ByteBuffer key();

    /**
     * 获取值的字节大小
     * @return 值的大小，如果值为null，则返回-1
     */
    int valueSize();

    /**
     * 检查是否存在值（即，该值是否为空）
     * @return 如果是，则为true，否则为false
     */
    boolean hasValue();

    /**
     * 获取记录的价值
     * @return （可空）值
     */
    ByteBuffer value();

    int n();

}
