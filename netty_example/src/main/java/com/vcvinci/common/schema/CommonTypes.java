package com.vcvinci.common.schema;

import com.vcvinci.common.Structable;
import com.vcvinci.common.exception.ThrowUtils;
import com.vcvinci.common.exception.common.SchemaException;
import com.vcvinci.common.exception.server.storage.InvalidBatchException;
import com.vcvinci.common.record.MemoryRecordBatch;
import com.vcvinci.common.record.MemoryRecordSet;
import com.vcvinci.common.record.RecordBatch;
import com.vcvinci.common.record.RecordSet;
import com.vcvinci.common.util.ByteUtils;
import com.vcvinci.common.util.Utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.nio.ByteBuffer;

/**
 * @author yupeng.sun@ucarinc.com
 * @version 1.0 创建时间：2017-12-14 下午1:54:46
 * @Description:
 */
public class CommonTypes {

    public static final Type<Boolean> BOOLEAN = new Type<Boolean>() {
        @Override
        public void write(ByteBuffer buffer, Boolean o) {
            if (o) {
                buffer.put((byte) 1);
            } else {
                buffer.put((byte) 0);
            }
        }

        @Override
        public Boolean read(ByteBuffer buffer) {
            byte value = buffer.get();
            return value != 0;
        }

        @Override
        public int sizeOf(Boolean o) {
            return 1;
        }

        @Override
        public String toString() {
            return "BOOLEAN";
        }

        @Override
        public Boolean validate(Object item) {
            if (item instanceof Boolean) {
                return (Boolean) item;
            } else {
                throw new SchemaException(item + " is not a Boolean.");
            }
        }
    };

    public static final Type<Byte> INT8 = new Type<Byte>() {
        @Override
        public void write(ByteBuffer buffer, Byte o) {
            buffer.put(o);
        }

        @Override
        public Byte read(ByteBuffer buffer) {
            return buffer.get();
        }

        @Override
        public int sizeOf(Byte o) {
            return 1;
        }

        @Override
        public String toString() {
            return "INT8";
        }

        @Override
        public Byte validate(Object item) {
            if (item instanceof Byte) {
                return (Byte) item;
            } else {
                throw new SchemaException(item + " is not a Byte.");
            }
        }
    };

    public static final Type<Short> INT16 = new Type<Short>() {
        @Override
        public void write(ByteBuffer buffer, Short o) {
            buffer.putShort(o);
        }

        @Override
        public Short read(ByteBuffer buffer) {
            return buffer.getShort();
        }

        @Override
        public int sizeOf(Short o) {
            return 2;
        }

        @Override
        public String toString() {
            return "INT16";
        }

        @Override
        public Short validate(Object item) {
            if (item instanceof Short) {
                return (Short) item;
            } else {
                throw new SchemaException(item + " is not a Short.");
            }
        }
    };

    public static final Type<Integer> INT32 = new Type<Integer>() {
        @Override
        public void write(ByteBuffer buffer, Integer o) {
            buffer.putInt(o);
        }

        @Override
        public Integer read(ByteBuffer buffer) {
            return buffer.getInt();
        }

        @Override
        public int sizeOf(Integer o) {
            return 4;
        }

        @Override
        public String toString() {
            return "INT32";
        }

        @Override
        public Integer validate(Object item) {
            if (item instanceof Integer) {
                return (Integer) item;
            } else {
                throw new SchemaException(item + " is not an Integer.");
            }
        }
    };

    public static final Type<Long> INT64 = new Type<Long>() {
        @Override
        public void write(ByteBuffer buffer, Long o) {
            buffer.putLong(o);
        }

        @Override
        public Long read(ByteBuffer buffer) {
            return buffer.getLong();
        }

        @Override
        public int sizeOf(Long o) {
            return 8;
        }

        @Override
        public String toString() {
            return "INT64";
        }

        @Override
        public Long validate(Object item) {
            if (item instanceof Long) {
                return (Long) item;
            } else {
                throw new SchemaException(item + " is not a Long.");
            }
        }
    };

    public static final Type<String> STRING = new Type<String>() {
        @Override
        public void write(ByteBuffer buffer, String o) {
            byte[] bytes = Utils.utf8(o);
            if (bytes.length > Short.MAX_VALUE) {
                throw new SchemaException(
                        "String length " + bytes.length + " is larger than the maximum string length.");
            }
            buffer.putShort((short) bytes.length);
            buffer.put(bytes);
        }

        @Override
        public String read(ByteBuffer buffer) {
            short length = buffer.getShort();
            if (length < 0) {
                throw new SchemaException("String length " + length + " cannot be negative");
            }
            if (length > buffer.remaining()) {
                throw new SchemaException("Error reading string of length " + length + ", only " + buffer.remaining()
                        + " bytes available");
            }
            String result = Utils.utf8(buffer, length);
            buffer.position(buffer.position() + length);
            return result;
        }

        @Override
        public int sizeOf(String o) {
            return 2 + Utils.utf8Length(o);
        }

        @Override
        public String toString() {
            return "STRING";
        }

        @Override
        public String validate(Object item) {
            if (item instanceof String) {
                return (String) item;
            } else {
                throw new SchemaException(item + " is not a String.");
            }
        }
    };

    public static final Type<String> NULLABLE_STRING = new Type<String>() {
        @Override
        public boolean isNullable() {
            return true;
        }

        @Override
        public void write(ByteBuffer buffer, String o) {
            if (o == null) {
                buffer.putShort((short) -1);
                return;
            }

            byte[] bytes = Utils.utf8(o);
            if (bytes.length > Short.MAX_VALUE) {
                throw new SchemaException(
                        "String length " + bytes.length + " is larger than the maximum string length.");
            }
            buffer.putShort((short) bytes.length);
            buffer.put(bytes);
        }

        @Override
        @SuppressWarnings("all")
        public String read(ByteBuffer buffer) {
            short length = buffer.getShort();
            if (length < 0) {
                return null;
            }
            if (length > buffer.remaining()) {
                throw new SchemaException("Error reading string of length " + length + ", only " + buffer.remaining()
                        + " bytes available");
            }
            String result = Utils.utf8(buffer, length);
            buffer.position(buffer.position() + length);
            return result;
        }

        @Override
        public String validate(Object o) {
            if (o == null) {
                return null;
            }
            if (o instanceof String) {
                return (String) o;
            } else {
                throw new SchemaException(o + " is not a String.");
            }
        }

        @Override
        public int sizeOf(String o) {
            if (o == null) {
                return 2;
            }

            return 2 + Utils.utf8Length(o);
        }

        @Override
        public String toString() {
            return "NULLABLE_STRING";
        }
    };

    public static final Type<ByteBuffer> DATA = new Type<ByteBuffer>() {
        @Override
        public void write(ByteBuffer buffer, ByteBuffer data) {
            if (data == null) {
                buffer.putInt(-1);
                return;
            }

            if (data.remaining() > Short.MAX_VALUE) {
                throw new SchemaException(
                        "String length " + data.remaining() + " is larger than the maximum string length.");
            }

            buffer.putInt(data.remaining());
            buffer.put(data);
        }

        @Override
        public ByteBuffer read(ByteBuffer buffer) {
            int length = buffer.getInt();
            if (length < 0) {
                return null;
            }

            if (length > buffer.remaining()) {
                throw new SchemaException("Error reading string of length " + length + ", only " + buffer.remaining()
                        + " bytes available");
            }
            ByteBuffer val = buffer.slice();
            val.limit(length);
            buffer.position(buffer.position() + length);
            return val;
        }

        @Override
        public int sizeOf(ByteBuffer buffer) {
            if (buffer == null) {
                return 4;
            }
            return buffer.remaining() + 4;
        }

        @Override
        public String toString() {
            return "ByteBuffer";
        }

        @Override
        public ByteBuffer validate(Object item) {
            if (item instanceof ByteBuffer) {
                return (ByteBuffer) item;
            } else {
                throw new SchemaException(item + " is not a java.nio.ByteBuffer.");
            }
        }
    };

    public static final Type<ByteBuffer> NULLABLE_DATA = new Type<ByteBuffer>() {
        @Override
        public boolean isNullable() {
            return true;
        }

        @Override
        public void write(ByteBuffer buffer, ByteBuffer data) {
            if (data == null) {
                buffer.putInt(-1);
                return;
            }

            if (data.remaining() > Short.MAX_VALUE) {
                throw new SchemaException(
                        "String length " + data.remaining() + " is larger than the maximum string length.");
            }

            buffer.putInt(data.remaining());
            data.mark();
            buffer.put(data);
            data.reset();
        }

        @Override
        public ByteBuffer read(ByteBuffer buffer) {

            int length = buffer.getInt();
            if (length < 0) {
                return null;
            }

            if (length > buffer.remaining()) {
                throw new SchemaException("Error reading string of length " + length + ", only " + buffer.remaining()
                        + " bytes available");
            }
            ByteBuffer val = buffer.slice();
            val.limit(length);
            buffer.position(buffer.position() + length);
            return val;
        }

        @Override
        public int sizeOf(ByteBuffer buffer) {
            if (buffer == null) {
                return 4;
            }
            return buffer.remaining() + 4;
        }

        @Override
        public String toString() {
            return "ByteBuffer";
        }

        @Override
        public ByteBuffer validate(Object item) {
            if (item instanceof ByteBuffer) {
                return (ByteBuffer) item;
            } else {
                throw new SchemaException(item + " is not a java.nio.ByteBuffer.");
            }
        }
    };

    public static final class STRUCT extends Type<Struct> {

        private Schema schema;

        public STRUCT(Schema schema) {
            this.schema = schema;
        }

        @Override
        public void write(ByteBuffer buffer, Struct struct) {
            // struct.writeTo(buffer);

            this.schema.write(buffer, struct);
        }

        @Override
        public Struct read(ByteBuffer buffer) {
            return schema.read(buffer);
        }

        @Override
        public Struct validate(Object o) {
            if (o instanceof Struct) {
                return (Struct) o;
            } else {
                throw new SchemaException(o + " is not a struct.");
            }
        }

        @Override
        public int sizeOf(Struct struct) {
            return struct.sizeOf();
        }

        public Schema schema() {
            return schema;
        }

    }

    public static final class ARRAY<T> extends Type<T[]> {

        private final Type<T> type;
        private final boolean nullable;

        public ARRAY(Type<T> type) {
            this(type, false);
        }

        private ARRAY(Type<T> type, boolean nullable) {
            this.type = type;
            this.nullable = nullable;
        }

        @Override
        public boolean isNullable() {
            return nullable;
        }

        @Override
        public void write(ByteBuffer buffer, T[] objs) {
            if (objs == null) {
                buffer.putInt(-1);
                return;
            }

            int size = objs.length;
            buffer.putInt(size);

            for (T obj : objs) {
                type.write(buffer, obj);
            }
        }

        @Override
        public T[] read(ByteBuffer buffer) {
            int size = buffer.getInt();
            if (size < 0 && isNullable()) {
                return null;
            } else if (size < 0) {
                throw new SchemaException("Array size " + size + " cannot be negative");
            }

            if (size > buffer.remaining()) {
                throw new SchemaException(
                        "Error reading array of size " + size + ", only " + buffer.remaining() + " bytes available");
            }
            ParameterizedType parameterizedType = (ParameterizedType) type.getClass().getGenericSuperclass();
            java.lang.reflect.Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            @SuppressWarnings("unchecked")
            T[] objs = (T[]) Array.newInstance((Class<?>) actualTypeArguments[0], size);
            for (int i = 0; i < size; i++) {
                objs[i] = type.read(buffer);
            }
            return objs;
        }

        @Override
        public int sizeOf(T[] objs) {
            int size = 4;
            if (objs == null) {
                return size;
            }

            for (T obj : objs) {
                size += type.sizeOf(obj);
            }
            return size;
        }

        public Type<T> type() {
            return type;
        }

        @Override
        public String toString() {
            return "ARRAY(" + type + ")";
        }

        @Override
        public T[] validate(Object item) {
            ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
            java.lang.reflect.Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            @SuppressWarnings("unchecked")
            Class<T[]> arrayClass = (Class<T[]>) actualTypeArguments[0];

            try {
                if (isNullable() && item == null) {
                    return null;
                }

                T[] array = arrayClass.cast(item);
                for (T obj : array) {
                    type.validate(obj);
                }
                return array;
            } catch (ClassCastException e) {
                throw new SchemaException("Not an " + arrayClass);
            }
        }
    }



    public static final Type<MemoryRecordBatch> MEMORY_RECORD_BATCH = new Type<MemoryRecordBatch>() {

        @Override
        public void write(ByteBuffer buffer, MemoryRecordBatch record) {
            try {
                record.writeTo(buffer);
            } catch (InvalidBatchException e) {
                throw ThrowUtils.eraseChecked(e);
            }
        }

        @Override
        public MemoryRecordBatch read(ByteBuffer buffer) {
            try {
                MemoryRecordBatch batch = new MemoryRecordBatch(buffer);
                batch.ensureValid();
                return batch;
            } catch (InvalidBatchException e) {
                throw ThrowUtils.eraseChecked(e);
            }
        }

        @Override
        public int sizeOf(MemoryRecordBatch o) {
            if (o != null) {
                try {
                    return o.sizeInBytes();
                } catch (InvalidBatchException e) {
                    throw ThrowUtils.eraseChecked(e);
                }
            } else {
                throw new RuntimeException("Type of MemoryRecordBatch is mismatch!");
            }
        }

        @Override
        public String toString() {
            return "RECORD_BATCH";
        }

        @Override
        public MemoryRecordBatch validate(Object item) {
            if (item instanceof MemoryRecordBatch) {
                return (MemoryRecordBatch) item;
            } else {
                throw new SchemaException(item + " is not a RecordBatch.");
            }
        }
    };

    public static final Type<RecordSet> RECORD_SET = new Type<RecordSet>() {
        @Override
        public boolean isNullable() {
            return true;
        }

        @Override
        public void write(ByteBuffer buffer, RecordSet records) {
            if (records == null) {
                buffer.putInt(-1);
                return;
            }
            buffer.putInt(records.sizeInBytes());
            int startPosition = buffer.position();
            // FIXME: 优化支持零拷贝
            try {
                for (RecordBatch batch : records.batches()) {
                    batch.writeTo(buffer);
                }
                buffer.position(startPosition + records.sizeInBytes());
            } catch (InvalidBatchException e) {
                throw ThrowUtils.eraseChecked(e);
            }
        }

        @Override
        public RecordSet read(ByteBuffer buffer) {
            int size = buffer.getInt();
            if (size < 0) {
                return null;
            }
            if (size > buffer.remaining()) {
                throw new SchemaException(
                        "Error reading bytes of size " + size + ", only " + buffer.remaining() + " bytes available");
            }

            ByteBuffer val = buffer.slice();
            val.limit(size);
            buffer.position(buffer.position() + size);
            return new MemoryRecordSet(val);
        }

        @Override
        public int sizeOf(RecordSet records) {
            if (records == null) {
                return 4;
            }

            return 4 + records.sizeInBytes();
        }

        @Override
        public String toString() {
            return "RECORD_SET";
        }

        @Override
        public RecordSet validate(Object item) {
            if (item == null) {
                return null;
            }

            if (item instanceof RecordSet) {
                return (RecordSet) item;
            }

            throw new SchemaException(item + " is not an instance of " + RecordSet.class.getName());
        }
    };

    public static final Type VARINT = new Type() {
        @Override
        public void write(ByteBuffer buffer, Object o) {
            ByteUtils.writeVarint((Integer) o, buffer);
        }

        @Override
        public Integer read(ByteBuffer buffer) {
            return ByteUtils.readVarint(buffer);
        }

        @Override
        public Integer validate(Object item) {
            if (item instanceof Integer) {
                return (Integer) item;
            }
            throw new SchemaException(item + " is not an integer");
        }

        @Override
        public String toString() {
            return "VARINT";
        }

        @Override
        public int sizeOf(Object o) {
            return ByteUtils.sizeOfVarint((Integer) o);
        }
    };

    public static final Type VARLONG = new Type() {
        @Override
        public void write(ByteBuffer buffer, Object o) {
            ByteUtils.writeVarlong((Long) o, buffer);
        }

        @Override
        public Long read(ByteBuffer buffer) {
            return ByteUtils.readVarlong(buffer);
        }

        @Override
        public Long validate(Object item) {
            if (item instanceof Long) {
                return (Long) item;
            }
            throw new SchemaException(item + " is not a long");
        }

        @Override
        public String toString() {
            return "VARLONG";
        }

        @Override
        public int sizeOf(Object o) {
            return ByteUtils.sizeOfVarlong((Long) o);
        }
    };

    public static void main(String[] args) {

        ParameterizedType parameterizedType = (ParameterizedType) new ARRAY<String>(STRING).type.getClass()
                .getGenericSuperclass();
        java.lang.reflect.Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        for (java.lang.reflect.Type actualTypeArgument : actualTypeArguments) {
            System.out.println(actualTypeArgument);
            String[] objs = (String[]) Array.newInstance((Class<?>) actualTypeArgument, 1);
            System.out.println(objs);
        }

    }

    /**
     * 使用示例：
     *
     * <pre>
     *     class Xxx implements Structable {
     *         static final Schema XXX_V0 = ...
     *
     *         static final Type<Xxx> XXX_TYPE = new TypeForSchema<Xxx>(XXX_V0) {};
     *
     *         // 这个构造函数必须有
     *         Xxx(Struct struct) {
     *            ...
     *         }
     *
     *     }
     * </pre>
     * 
     * @param <T>
     */
    public static abstract class TypeForSchema<T extends Structable> extends Type<T> {
        private final Schema schema;

        private final Constructor<T> constructor;

        public TypeForSchema(Schema schema) {
            ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
            @SuppressWarnings("unchecked")
            Class<T> classOfType = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
            try {
                constructor = classOfType.getConstructor(Struct.class);
                constructor.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw ThrowUtils.eraseChecked(e);
            }

            this.schema = schema;
        }

        @Override
        public void write(ByteBuffer buffer, T o) {
            o.toStruct().writeTo(buffer);
        }

        @Override
        public T read(ByteBuffer buffer) {
            try {
                return this.constructor.newInstance(this.schema.read(buffer));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw ThrowUtils.eraseChecked(e);
            }
        }

        @Override
        public T validate(Object o) {
            if (o != null && o.getClass() == this.constructor.getDeclaringClass()) {
                return (T) o;
            }
            throw new SchemaException(o + " is not instance of " + this.constructor.getDeclaringClass());
        }

        @Override
        public int sizeOf(T o) {
            return o.toStruct().sizeOf();
        }
    }
}
