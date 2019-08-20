package com.vcvinci.common.exception.common;

/**
 * Thrown if the protocol schema validation fails while parsing request or response.
 */
public class SchemaException extends CommonException {

    private static final long serialVersionUID = 1L;

    public SchemaException(String message) {
        super(message);
    }

}
