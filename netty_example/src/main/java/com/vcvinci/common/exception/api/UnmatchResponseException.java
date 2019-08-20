package com.vcvinci.common.exception.api;

public class UnmatchResponseException extends ApiException {

    private static final long serialVersionUID = 1L;

    public UnmatchResponseException() {
    }

    public UnmatchResponseException(String message) {
        super(message);
    }

    public UnmatchResponseException(Throwable cause) {
        super(cause);
    }

    public UnmatchResponseException(String message, Throwable cause) {
        super(message, cause);
    }

}
