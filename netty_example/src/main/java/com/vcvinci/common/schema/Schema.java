package com.vcvinci.common.schema;

import com.vcvinci.common.exception.common.SchemaException;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yupeng.sun@ucarinc.com
 * @version 1.0 创建时间：2017-12-14 上午9:59:53
 * @Description:
 */
public class Schema extends Type<Struct> {

    private FieldWrapper[] fields;
    private Map<String, FieldWrapper> fieldsByName;

    public Schema(Field... fs) {
        fields = new FieldWrapper[fs.length];
        fieldsByName = new HashMap<>(fs.length);
        for (int i = 0; i < fields.length; i++) {
            if (fieldsByName.containsKey(fs[i].getName())) {
                throw new SchemaException("Schema contains a duplicate field: " + fs[i].getName());
            }
            fields[i] = new FieldWrapper(fs[i], i, this);
            fieldsByName.put(fs[i].getName(), fields[i]);
        }
    }

    public FieldWrapper get(String name) {
        return fieldsByName.get(name);
    }

    public FieldWrapper get(int index) {
        return fields[index];
    }

    @Override
    public void write(ByteBuffer buffer, Struct struct) {
        for (FieldWrapper field : fields) {
            try {
                Type type = field.getField().getType();
                Object value = struct.get(field);
                type.write(buffer, value);
            } catch (Exception e) {
                throw new SchemaException("Error writing field '"
                        + field.getField().getName()
                        + "': "
                        + (e.getMessage() == null ? e.getClass().getName()
                        : e.getMessage()));
            }
        }
    }

    @Override
    public Struct read(ByteBuffer buffer) {
        Object[] objects = new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            try {
                Type type = fields[i].getField().getType();
                objects[i] = type.read(buffer);
            } catch (Exception e) {
                throw new SchemaException("Error reading field '"
                        + fields[i].getField().getName()
                        + "': "
                        + (e.getMessage() == null ? e.getClass().getName()
                        : e.getMessage()));
            }
        }
        return new Struct(this, objects);
    }

    @Override
    public int sizeOf(Struct struct) {
        int size = 0;
        for (FieldWrapper field : fields) {
            try {
                Type type = field.getField().getType();
                size += type.sizeOf(struct.get(field));
            } catch (Exception e) {
                throw new SchemaException("Error computing size for field '"
                        + field.getField().getName()
                        + "': "
                        + (e.getMessage() == null ? e.getClass().getName()
                        : e.getMessage()));
            }
        }
        return size;
    }

    @Override
    public Struct validate(Object item) {
        try {
            Struct struct = (Struct) item;
            for (FieldWrapper field : fields) {
                try {
                    field.field.type.validate(struct.get(field));
                } catch (SchemaException e) {
                    throw new SchemaException("Invalid value for field '" + field.field.name + "': " + e.getMessage());
                }
            }
            return struct;
        } catch (ClassCastException e) {
            throw new SchemaException("Not a Struct.");
        }
    }

    public int numFields() {
        return this.fields.length;
    }
}
