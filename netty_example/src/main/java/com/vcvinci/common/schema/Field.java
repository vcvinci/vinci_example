package com.vcvinci.common.schema;

import java.io.Serializable;

/**
 * @author yupeng.sun@ucarinc.com
 * @version 1.0 创建时间：2017-12-14 上午10:50:22
 * @Description:
 */
public class Field implements Serializable {

    private static final long serialVersionUID = -786826951140760192L;

    public final String name;
    public final String desc;
    public final Type<?> type;
    public final Object defaultValue;

    public Field(String name, Type<?> type) {
        this(name, type, null, null);
    }

    public Field(String name, Type<?> type, String desc) {
        this(name, type, null, desc);
    }

    public Field(String name, Type<?> type, Object defaultValue, String desc) {
        this.name = name;
        this.desc = desc;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public Type getType() {
        return type;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

}
