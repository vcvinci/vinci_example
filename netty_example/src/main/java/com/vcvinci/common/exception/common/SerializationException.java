package com.vcvinci.common.exception.common;

import com.vcvinci.common.exception.DoveException;

/**
 * Any exception during serialization in the producer
 */
public class SerializationException extends DoveException {

    private static final long serialVersionUID = 1L;

    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }

    public SerializationException() {
        super();
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}