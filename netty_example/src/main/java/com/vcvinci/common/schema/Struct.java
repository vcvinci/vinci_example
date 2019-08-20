package com.vcvinci.common.schema;

import com.vcvinci.common.exception.common.SchemaException;
import com.vcvinci.common.record.MemoryRecordBatch;
import com.vcvinci.common.record.RecordSet;
import com.vcvinci.common.schema.CommonTypes.ARRAY;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * @author yupeng.sun@ucarinc.com
 * @version 1.0 创建时间：2017-12-14 下午1:25:45
 * @Description:
 */
public class Struct {

    private final Schema schema;
    private final Object[] values;

    public Struct(Schema schema) {
        this.schema = schema;
        this.values = new Object[this.schema.numFields()];
    }

    Struct(Schema schema, Object[] values) {
        this.schema = schema;
        this.values = values;
    }

    public Struct set(String name, Object value) {
        FieldWrapper field = schema.get(name);
        if (field == null) {
            throw new SchemaException("Unknown field: " + name);
        }
        values[field.getIndex()] = value;
        return this;
    }

    public Short getShort(String name) {
        return (Short) get(name);
    }

    public Integer getInt(String name) {
        return (Integer) get(name);
    }

    public Long getLong(String name) {
        return (Long) get(name);
    }

    public String getString(String name) {
        return (String) get(name);
    }

    public ByteBuffer getByteBuffer(String name) {
        return (ByteBuffer) get(name);
    }

    public Object get(FieldWrapper field) {
        validateField(field);
        return getFieldOrDefault(field);
    }

    public Object get(String name) {
        FieldWrapper field = schema.get(name);
        if (field == null) {
            throw new SchemaException("No such field: " + name);
        }
        return getFieldOrDefault(field);
    }

    private void validateField(FieldWrapper field) {
        if (this.schema != field.getSchema()) {
            throw new SchemaException(
                    "Attempt to access field '" + field.getField().getName() + "' from a different schema instance.");
        }
        if (field.getIndex() > values.length) {
            throw new SchemaException("Invalid field index: " + field.getIndex());
        }
    }

    private Object getFieldOrDefault(FieldWrapper field) {
        Object value = this.values[field.getIndex()];
        if (value != null) {
            return value;
        } else if (field.getField().getDefaultValue() != null) {
            return field.getField().getDefaultValue();
        } else if (field.getField().getType().isNullable()) {
            return null;
        } else {
            throw new SchemaException(
                    "Missing value for field '" + field.getField().getName() + "' which has no default value.");
        }
    }

    public int sizeOf() {
        return this.schema.sizeOf(this);
    }

    public void writeTo(ByteBuffer buffer) {
        this.schema.write(buffer, this);
    }

    /**
     * 功能描述:检查结构体是否包含字段。
     *
     * @param: [name]
     * @return: boolean
     * @auther: Administrator
     * @date: 2018/6/25 15:02
     */
    public boolean hasField(String name) {
        return schema.get(name) != null;
    }

    /**
     * 功能描述:为给定字段创建一个struct实例，该字段必须是容器类型(struct或array)
     *
     * @param: [field]
     * @return: com.vcvinci.protocol.type.Struct
     * @auther: Administrator
     * @date: 2018/6/25 15:03
     */
    public Struct instance(String field) {
        return instance(schema.get(field));
    }

    /**
     * 功能描述: 为容器类型的模式(结构体或数组)创建一个结构体。注意，对于数组类型，这个方法。 假设该类型是一个模式数组，并创建该模式的结构。其他类型的数组不能是 用这种方法实例化。
     *
     * @param: [fieldWrapper]
     * @return: com.vcvinci.protocol.type.Struct
     * @auther: Administrator
     * @date: 2018/6/25 15:03
     */
    public Struct instance(FieldWrapper fieldWrapper) {
        validateField(fieldWrapper);
        Type type = fieldWrapper.field.type;
        if (type instanceof Schema) {
            return new Struct((Schema) type);
        } else if (type instanceof ARRAY) {
            ARRAY<?> array = (ARRAY<?>) type;
            return new Struct((Schema) array.type());
        } else if (type instanceof CommonTypes.STRUCT) {
            CommonTypes.STRUCT struct = (CommonTypes.STRUCT) type;
            return new Struct(struct.schema());
        } else {
            throw new SchemaException(
                    "CommonField '" + fieldWrapper.field.name + "' is not a container type, it is of type " + type);
        }
    }

    public Struct set(CommonField.Str def, String value) {
        return set(def.name, value);
    }

    public Struct set(CommonField.Int32 def, int value) {
        return set(def.name, value);
    }

    public Struct set(CommonField.Int64 def, long value) {
        return set(def.name, value);
    }

    public Struct set(CommonField.Int16 def, short value) {
        return set(def.name, value);
    }

    public Struct set(CommonField.Int8 def, byte value) {
        return set(def.name, value);
    }

    public Boolean getBoolean(FieldWrapper field) {
        return (Boolean) get(field);
    }

    public Boolean getBoolean(String name) {
        return (Boolean) get(name);
    }

    public byte getByte(String name) {
        return (Byte) get(name);
    }

    public Byte getByte(FieldWrapper field) {
        return (Byte) get(field);
    }

    public Byte get(CommonField.Int8 field) {
        return getByte(field.name);
    }

    public Integer get(CommonField.Int32 field) {
        return getInt(field.name);
    }

    public Long get(CommonField.Int64 field) {
        return getLong(field.name);
    }

    public Short get(CommonField.Int16 field) {
        return getShort(field.name);
    }

    public String get(CommonField.Str field) {
        return getString(field.name);
    }

    public Object[] getArray(String name) {
        return (Object[]) get(name);
    }

    public Struct[] getStructArray(String name) {
        return (Struct[]) get(name);
    }

    public Struct getStruct(String name) {
        return (Struct) get(name);
    }

    public String[] getStringArray(String name) {
        return (String[]) get(name);
    }

    public Integer[] getIntegerArray(String name) {
        return (Integer[]) get(name);
    }

    public MemoryRecordBatch getDefaultRecordBatch(String name) {
        return (MemoryRecordBatch) get(name);
    }

    public RecordSet getRecordSet(String name) {
        return (RecordSet) get(name);
    }

    @Override
    public int hashCode() {
        return ArrayUtils.hashCode(this.values);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Struct))
            return false;
        Struct other = (Struct) obj;
        if (schema != other.schema)
            return false;
        return Objects.deepEquals(this.values, other.values);
    }
}
