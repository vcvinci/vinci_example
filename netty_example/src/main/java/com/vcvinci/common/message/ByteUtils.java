package com.vcvinci.common.message;

import java.io.UnsupportedEncodingException;

public class ByteUtils {

    public static final String DEFAULT_CHARSET_NAME = "utf-8";

    public static final byte[] getBytes(final String k) {
        if (k == null || k.length() == 0) {
            return null;
        }
        try {
            return k.getBytes(DEFAULT_CHARSET_NAME);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getString(final byte[] bytes) {
        try {
            return new String(bytes, DEFAULT_CHARSET_NAME);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
