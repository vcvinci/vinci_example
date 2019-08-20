package com.vcvinci.common.serialization;

import java.util.Map;

public class ShortSerializer implements Serializer<Short> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // nothing to do
    }

    @Override
    public byte[] serialize(String topic, Short data) {
        if (data == null) {
            return null;
        }

        return new byte[]{
                (byte) (data >>> 8),
                data.byteValue()
        };
    }

    @Override
    public void close() {
        // nothing to do
    }
}