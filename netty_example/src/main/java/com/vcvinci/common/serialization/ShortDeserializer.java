package com.vcvinci.common.serialization;

import com.vcvinci.common.exception.common.SerializationException;

import java.util.Map;

public class ShortDeserializer implements Deserializer<Short> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // nothing to do
    }

    @Override
    public Short deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        if (data.length != 2) {
            throw new SerializationException("Size of data received by ShortDeserializer is not 2");
        }

        short value = 0;
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
