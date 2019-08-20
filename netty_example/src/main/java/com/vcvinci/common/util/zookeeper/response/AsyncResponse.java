package com.vcvinci.common.util.zookeeper.response;

import org.apache.zookeeper.KeeperException;

public abstract class AsyncResponse {

    private KeeperException.Code resultCode;

    private String path;

    private Object ctx;

    public AsyncResponse() {
    }

    public AsyncResponse(KeeperException.Code resultCode, String path, Object ctx) {
        this.resultCode = resultCode;
        this.path = path;
        this.ctx = ctx;
    }

    public KeeperException.Code getResultCode() {
        return resultCode;
    }

    public void setResultCode(KeeperException.Code resultCode) {
        this.resultCode = resultCode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getCtx() {
        return ctx;
    }

    public void setCtx(Object ctx) {
        this.ctx = ctx;
    }
}
