package com.vcvinci.common.util.zookeeper;

public class ResponseMetadata {

    private long sendTimeMs;

    private long receivedTimeMs;

    public ResponseMetadata(long sendTimeMs, long receivedTimeMs) {
        this.sendTimeMs = sendTimeMs;
        this.receivedTimeMs = receivedTimeMs;
    }

    public long responseTimeMs() {
        return this.receivedTimeMs - sendTimeMs;
    }
}
