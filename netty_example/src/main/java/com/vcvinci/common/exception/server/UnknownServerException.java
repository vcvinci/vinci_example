package com.vcvinci.common.exception.server;

/**
 * An error occurred on the server for which the client doesn't have a corresponding error code. This is generally an
 * unexpected error.
 */
public class UnknownServerException extends ServerException {

    private static final long serialVersionUID = 1L;

    public UnknownServerException() {
    }

    public UnknownServerException(String message) {
        super(message);
    }

    public UnknownServerException(Throwable cause) {
        super(cause);
    }

    public UnknownServerException(String message, Throwable cause) {
        super(message, cause);
    }

}
