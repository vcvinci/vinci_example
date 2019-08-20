package com.vcvinci.common.serialization;

import com.vcvinci.common.exception.common.SerializationException;

import java.util.Map;

public class FloatDeserializer implements Deserializer<Float> {

    @Override
    public void configure(final Map<String, ?> configs, final boolean isKey) {
        // nothing to do
    }

    @Override
    public Float deserialize(final String topic, final byte[] data) {
        if (data == null) {
            return null;
        }
        if (data.length != 4) {
            throw new SerializationException("Size of data received by Deserializer is not 4");
        }

        int value = 0;
        for (byte b : data) {
            value <<= 8;
            value |= b & 0xFF;
        }
        return Float.intBitsToFloat(value);
    }

    @Override
    public void close() {
        // nothing to do
    }

}