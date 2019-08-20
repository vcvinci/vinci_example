package com.vcvinci.common.exception.server;

import com.vcvinci.common.exception.DoveException;

public class ServerException extends DoveException {

    private static final long serialVersionUID = 1L;

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(Throwable cause) {
        super(cause);
    }

    public ServerException() {
        super();
    }

}
