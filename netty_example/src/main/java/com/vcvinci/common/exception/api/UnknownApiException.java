package com.vcvinci.common.exception.api;

/**
 * An error occurred on the server for which the client doesn't have a corresponding error code. This is generally an
 * unexpected error.
 */
public class UnknownApiException extends ApiException {

    private static final long serialVersionUID = 1L;

    public UnknownApiException() {
    }

    public UnknownApiException(String message) {
        super(message);
    }

    public UnknownApiException(Throwable cause) {
        super(cause);
    }

    public UnknownApiException(String message, Throwable cause) {
        super(message, cause);
    }

}
