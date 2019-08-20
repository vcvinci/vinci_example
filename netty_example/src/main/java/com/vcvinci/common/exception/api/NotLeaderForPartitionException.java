package com.vcvinci.common.exception.api;

public class NotLeaderForPartitionException extends ApiException {

    private static final long serialVersionUID = 1L;

    public NotLeaderForPartitionException() {
    }

    public NotLeaderForPartitionException(String message) {
        super(message);
    }

    public NotLeaderForPartitionException(Throwable cause) {
        super(cause);
    }

    public NotLeaderForPartitionException(String message, Throwable cause) {
        super(message, cause);
    }

}
