package com.vcvinci.common.exception.api;

import com.vcvinci.common.exception.RetriableException;

/**
 * An exception that may indicate the client's metadata is out of date
 */
public abstract class InvalidMetadataException extends ApiException implements RetriableException {

    private static final long serialVersionUID = 1L;

    public InvalidMetadataException() {
        super();
    }

    public InvalidMetadataException(String message) {
        super(message);
    }

    public InvalidMetadataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMetadataException(Throwable cause) {
        super(cause);
    }

}
