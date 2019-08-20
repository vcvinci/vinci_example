package com.vcvinci.common.util.zookeeper.response;

import com.vcvinci.common.util.zookeeper.ResponseMetadata;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

public class SetDataResponse extends AsyncResponse {

    private Stat stat;

    private ResponseMetadata metadata;

    public SetDataResponse(KeeperException.Code resultCode, String path, Object ctx, Stat stat, ResponseMetadata metadata) {
        super(resultCode, path, ctx);
        this.stat = stat;
        this.metadata = metadata;
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
