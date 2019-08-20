package com.vcvinci.common.util.zookeeper.response;

import com.vcvinci.common.util.zookeeper.ResponseMetadata;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

public class GetDataResponse extends AsyncResponse {

    private byte[] data;

    private Stat stat;

    private ResponseMetadata metadata;

    public GetDataResponse(KeeperException.Code resultCode, String path, Object ctx, byte[] data, Stat stat, ResponseMetadata metadata) {
        super(resultCode, path, ctx);
        this.data = data;
        this.stat = stat;
        this.metadata = metadata;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Stat getStat() {
        return stat;
    }

    public void setStat(Stat stat) {
        this.stat = stat;
    }

    public ResponseMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ResponseMetadata metadata) {
        this.metadata = metadata;
    }
}
