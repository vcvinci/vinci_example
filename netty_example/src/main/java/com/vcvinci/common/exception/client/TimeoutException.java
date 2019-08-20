package com.vcvinci.common.exception.client;

import com.vcvinci.common.exception.RetriableException;

public class TimeoutException extends ClientException implements RetriableException {

    private static final long serialVersionUID = 1L;

    public TimeoutException() {
        super();
    }

    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeoutException(String message) {
        super(message);
    }

    public TimeoutException(Throwable cause) {
        super(cause);
    }
}
