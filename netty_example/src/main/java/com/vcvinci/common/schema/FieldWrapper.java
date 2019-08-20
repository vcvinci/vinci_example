package com.vcvinci.common.schema;

/**
 * @author yupeng.sun@ucarinc.com
 * @version 1.0 创建时间：2017-12-14 上午11:03:34
 * @Description:
 */
public class FieldWrapper {

    public final Field field;
    final int index;
    final Schema schema;

    public FieldWrapper(Field field, int index, Schema schema) {
        this.field = field;
        this.index = index;
        this.schema = schema;
    }

    public Field getField() {
        return field;
    }

    public int getIndex() {
        return index;
    }

    public Schema getSchema() {
        return schema;
    }

}
