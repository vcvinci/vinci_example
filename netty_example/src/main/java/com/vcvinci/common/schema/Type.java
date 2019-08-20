package com.vcvinci.common.schema;

import java.nio.ByteBuffer;

/**
 * @author yupeng.sun@ucarinc.com
 * @version 1.0 创建时间：2017-12-14 上午10:50:10
 * @Description:
 */
public abstract class Type<T> {

    public abstract void write(ByteBuffer buffer, T o);

    public abstract T read(ByteBuffer buffer);

    public abstract T validate(Object o);

    public abstract int sizeOf(T o);

    public boolean isNullable() {
        return false;
    }

}
