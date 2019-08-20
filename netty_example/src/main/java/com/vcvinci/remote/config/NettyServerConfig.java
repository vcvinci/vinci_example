package com.vcvinci.remote.config;

import com.ucarinc.dove.config.bootstrap.AbstractBootstrapConfig;
import com.ucarinc.dove.config.bootstrap.BootstrapConfigConstant;

public class NettyServerConfig extends AbstractConfig {

    public NettyServerConfig(AbstractBootstrapConfig config) {
        super(config.getBoolean(BootstrapConfigConstant.BYTEBUF_POOL_CONFIG));
        this.port = config.getInt(BootstrapConfigConstant.SERVER_PORT);
        this.serverWorkerCount = config.getInt(BootstrapConfigConstant.NETTY_SERVER_WORKER_THREAD_COUNT_CONFIG);
        this.serverSocketSndBufSize = config.getInt(BootstrapConfigConstant.SO_SNDBUF_CONFIG);
        this.serverSocketRcvBufSize = config.getInt(BootstrapConfigConstant.SO_RCVBUF_CONFIG);

        this.soBacklog = config.getInt(BootstrapConfigConstant.SO_BACKLOG_CONFIG);
        this.tcpNoDelay = config.getBoolean(BootstrapConfigConstant.TCP_NODELAY_CONFIG);
        this.soReuseaddr = config.getBoolean(BootstrapConfigConstant.SO_REUSEADDR_CONFIG);
        this.soKeepalive = config.getBoolean(BootstrapConfigConstant.SO_KEEPALIVE_CONFIG);
        this.readerIdleTimeSeconds = config.getInt(BootstrapConfigConstant.READER_IDLE_TIME_SECONDS_CONFIG);
        this.writerIdleTimeSeconds = config.getInt(BootstrapConfigConstant.WRITER_IDLE_TIME_SECONDS_CONFIG);
        this.serverChannelMaxIdleTimeSeconds = config.getInt(BootstrapConfigConstant.ALL_IDLE_TIME_SECONDS_CONFIG);
        this.connectTimeoutMs = config.getInt(BootstrapConfigConstant.CONNECT_TIMEOUT_MS_CONFIG);
    }

    private int port;
    private int serverWorkerCount;
    private int serverSocketSndBufSize;
    private int serverSocketRcvBufSize;
    private int serverChannelMaxIdleTimeSeconds;

    private int soBacklog;
    private boolean tcpNoDelay;
    private boolean soReuseaddr;
    private boolean soKeepalive;
    private int readerIdleTimeSeconds;
    private int writerIdleTimeSeconds;
    private int connectTimeoutMs;

    public int getPort() {
        return port;
    }

    public int getServerWorkerCount() {
        return serverWorkerCount;
    }

    public int getServerSocketSndBufSize() {
        return serverSocketSndBufSize;
    }

    public int getServerSocketRcvBufSize() {
        return serverSocketRcvBufSize;
    }

    public int getServerChannelMaxIdleTimeSeconds() {
        return serverChannelMaxIdleTimeSeconds;
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

    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public int getSoBacklog() {
        return soBacklog;
    }
}