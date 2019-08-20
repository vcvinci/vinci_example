package com.vcvinci.common.record;

import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;

import java.nio.ByteBuffer;
import java.util.Objects;

public class DefaultRecord implements Record {

    private static final Schema DEFAULT_RECORD_V0 = new Schema(
            new Field(ProtocolSchemaConstant.KEY_KEY_NAME, CommonTypes.NULLABLE_DATA),
            new Field(ProtocolSchemaConstant.VALUE_KEY_NAME, CommonTypes.NULLABLE_DATA));

    private final Struct struct;

    private final int n;

    public DefaultRecord(ByteBuffer key, ByteBuffer value) {
        this.struct = new Struct(DEFAULT_RECORD_V0);
        this.struct.set(ProtocolSchemaConstant.KEY_KEY_NAME, key);
        this.struct.set(ProtocolSchemaConstant.VALUE_KEY_NAME, value);
        this.n = -1;
    }

    private DefaultRecord(ByteBuffer buffer, int n) {
        this.struct = DEFAULT_RECORD_V0.read(buffer);
        this.n = n;
    }

    public static DefaultRecord readFrom(ByteBuffer buffer, int n) {
        return new DefaultRecord(buffer, n);
    }

    public static Schema[] schemaVersions() {
        return new Schema[] { DEFAULT_RECORD_V0 };
    }

    @Override
    public Struct toStruct() {
        return this.struct;
    }

    @Override
    public int sizeInBytes() {
        return this.struct.sizeOf();
    }

    @Override
    public int keySize() {
        ByteBuffer key = this.key();
        return key == null ? 0 : key.remaining();
    }

    @Override
    public int valueSize() {
        ByteBuffer value = this.value();
        return value == null ? 0 : value.remaining();
    }

    @Override
    public boolean hasKey() {
        return this.key() != null;
    }

    @Override
    public ByteBuffer key() {
        return this.struct.getByteBuffer(ProtocolSchemaConstant.KEY_KEY_NAME);
    }

    @Override
    public boolean hasValue() {
        return this.value() != null;
    }

    @Override
    public ByteBuffer value() {
        return this.struct.getByteBuffer(ProtocolSchemaConstant.VALUE_KEY_NAME);
    }

    @Override
    public int n() {
        return this.n;
    }

    @Override
    public String toString() {
        return String.format("DefaultRecord(key=%d bytes, value=%d bytes)", this.keySize(), this.valueSize());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultRecord)) {
            return false;
        }

        DefaultRecord that = (DefaultRecord) o;
        return Objects.equals(this.key(), that.key()) && Objects.equals(this.value(), that.value());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key(), this.value());
    }

}
