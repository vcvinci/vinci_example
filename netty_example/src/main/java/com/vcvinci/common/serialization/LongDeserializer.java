package com.vcvinci.common.serialization;

import com.vcvinci.common.exception.common.SerializationException;

import java.util.Map;

public class LongDeserializer implements Deserializer<Long> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // nothing to do
    }

    @Override
    public Long deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        if (data.length != 8) {
            throw new SerializationException("Size of data received by LongDeserializer is not 8");
        }

        long value = 0;
        for (byte b : data) {
            value <<= 8;
            value |= b & 0xFF;
        }
        return value;
    }

    @Override
    public void close() {
        // nothing to do
    }
}
