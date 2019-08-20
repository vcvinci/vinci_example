package com.vcvinci.common.serialization;

import com.ucarinc.dove.util.Bytes;

import java.util.Map;

public class BytesDeserializer implements Deserializer<Bytes> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // nothing to do
    }

    @Override
    public Bytes deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        return new Bytes(data);
    }

    @Override
    public void close() {
        // nothing to do
    }
}
