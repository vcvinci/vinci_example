package com.vcvinci.common;

import com.vcvinci.common.schema.Struct;

/**
 * 为了方便序列化
 * @author vinci
 * @date 2018/12/3下午1:43
 */
public interface Structable {
    /**
     * 功能描述: 生成可序列化的实例
     * @return 可序列化的实例
     * @author vinci
     * @date 2018/12/3 下午1:43
     */
    Struct toStruct();
}
