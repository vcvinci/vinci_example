package com.vcvinci.common.serialization;

import com.ucarinc.dove.util.Bytes;

import java.util.Map;

public class BytesSerializer implements Serializer<Bytes> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // nothing to do
    }

    @Override
    public byte[] serialize(String topic, Bytes data) {
        if (data == null) {
            return null;
        }

        return data.get();
    }

    @Override
    public void close() {
        // nothing to do
    }
}

