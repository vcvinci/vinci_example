package com.vcvinci.common.util.zookeeper.request;

public class SetDataRequest extends AsyncRequest {

    private byte[] data;

    private int version;

    public SetDataRequest(String path, Object ctx, byte[] data, int version) {
        super(path, ctx);
        this.data = data;
        this.version = version;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
