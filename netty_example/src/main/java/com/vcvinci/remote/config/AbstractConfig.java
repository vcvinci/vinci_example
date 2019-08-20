package com.vcvinci.remote.config;

public class AbstractConfig {

    protected AbstractConfig(boolean bytebufPool) {
        this.bytebufPool = bytebufPool;
    }

    private boolean bytebufPool = false;

    public boolean isBytebufPool() {
        return bytebufPool;
    }

    public void setBytebufPool(boolean bytebufPool) {
        this.bytebufPool = bytebufPool;
    }

}
