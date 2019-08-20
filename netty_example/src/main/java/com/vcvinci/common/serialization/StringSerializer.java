package com.vcvinci.common.serialization;

import com.vcvinci.common.exception.common.SerializationException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * String encoding defaults to UTF8 and can be customized by setting the property key.serializer.encoding,
 * value.serializer.encoding or serializer.encoding. The first two take precedence over the last.
 */
public class StringSerializer implements Serializer<String> {

    private String encoding = "UTF8";

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        String propertyName = isKey ? "key.serializer.encoding" : "value.serializer.encoding";
        Object encodingValue = configs.get(propertyName);
        if (encodingValue == null) {
            encodingValue = configs.get("serializer.encoding");
        }
        if (encodingValue != null && encodingValue instanceof String) {
            encoding = (String) encodingValue;
        }
    }

    @Override
    public byte[] serialize(String topic, String data) {
        try {
            if (data == null) {
                return null;
            } else {
                return data.getBytes(encoding);
            }
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException("Error when serializing string to byte[] due to unsupported encoding " + encoding);
        }
    }

    @Override
    public void close() {
        // nothing to do
    }
}