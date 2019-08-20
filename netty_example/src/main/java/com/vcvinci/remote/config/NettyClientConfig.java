package com.vcvinci.remote.config;

import com.ucarinc.dove.config.bootstrap.AbstractBootstrapConfig;
import com.ucarinc.dove.config.bootstrap.BootstrapConfigConstant;

public class NettyClientConfig extends AbstractConfig {

    private boolean tcpNoDelay;
    private boolean soReuseaddr;
    private boolean soKeepalive;
    private int readerIdleTimeSeconds;
    private int writerIdleTimeSeconds;
    private int allIdleTimeSeconds;
    private int connectTimeoutMs;

    public NettyClientConfig(AbstractBootstrapConfig config) {
        super(config.getBoolean(BootstrapConfigConstant.BYTEBUF_POOL_CONFIG));
        this.tcpNoDelay = config.getBoolean(BootstrapConfigConstant.TCP_NODELAY_CONFIG);
        this.soReuseaddr = config.getBoolean(BootstrapConfigConstant.SO_REUSEADDR_CONFIG);
        this.soKeepalive = config.getBoolean(BootstrapConfigConstant.SO_KEEPALIVE_CONFIG);
        this.readerIdleTimeSeconds = config.getInt(BootstrapConfigConstant.READER_IDLE_TIME_SECONDS_CONFIG);
        this.writerIdleTimeSeconds = config.getInt(BootstrapConfigConstant.WRITER_IDLE_TIME_SECONDS_CONFIG);
        this.allIdleTimeSeconds = config.getInt(BootstrapConfigConstant.ALL_IDLE_TIME_SECONDS_CONFIG);
        this.connectTimeoutMs = config.getInt(BootstrapConfigConstant.CONNECT_TIMEOUT_MS_CONFIG);
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public boolean isSoReuseaddr() {
        return soReuseaddr;
    }

    public boolean isSoKeepalive() {
        return soKeepalive;
    }

    public int getReaderIdleTimeSeconds() {
        return readerIdleTimeSeconds;
    }

    public int getWriterIdleTimeSeconds() {
        return writerIdleTimeSeconds;
    }

    public int getAllIdleTimeSeconds() {
        return allIdleTimeSeconds;
    }

    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }
}
