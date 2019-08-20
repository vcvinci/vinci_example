package com.vcvinci.common.serialization;

import java.util.Map;

public class FloatSerializer implements Serializer<Float> {

    @Override
    public void configure(final Map<String, ?> configs, final boolean isKey) {
        // nothing to do
    }

    @Override
    public byte[] serialize(final String topic, final Float data) {
        if (data == null) {
            return null;
        }

        long bits = Float.floatToRawIntBits(data);
        return new byte[]{
                (byte) (bits >>> 24),
                (byte) (bits >>> 16),
                (byte) (bits >>> 8),
                (byte) bits
        };
    }

    @Override
    public void close() {
        // nothing to do
    }
}