package com.vcvinci.common.util.zookeeper.request;

public class DeleteRequest extends AsyncRequest {

    private int version;

    public DeleteRequest(String path, Object ctx, int version) {
        super(path, ctx);
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
