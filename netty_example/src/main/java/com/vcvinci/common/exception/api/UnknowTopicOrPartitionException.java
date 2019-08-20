package com.vcvinci.common.exception.api;

public class UnknowTopicOrPartitionException extends ApiException {

    private static final long serialVersionUID = 1L;

    public UnknowTopicOrPartitionException() {
    }

    public UnknowTopicOrPartitionException(String message) {
        super(message);
    }

    public UnknowTopicOrPartitionException(Throwable cause) {
        super(cause);
    }

    public UnknowTopicOrPartitionException(String message, Throwable cause) {
        super(message, cause);
    }

}
