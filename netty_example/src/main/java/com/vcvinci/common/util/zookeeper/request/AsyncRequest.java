package com.vcvinci.common.util.zookeeper.request;

public abstract class AsyncRequest {

    private String path;

    private Object ctx;

    public AsyncRequest() {
    }

    public AsyncRequest(String path, Object ctx) {
        this.path = path;
        this.ctx = ctx;
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
