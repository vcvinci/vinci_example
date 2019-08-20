package com.vcvinci.common.exception.api;

public class AuthorizationException extends ApiException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

}
