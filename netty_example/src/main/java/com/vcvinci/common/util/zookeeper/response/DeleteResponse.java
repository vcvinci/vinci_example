package com.vcvinci.common.util.zookeeper.response;

import com.vcvinci.common.util.zookeeper.ResponseMetadata;
import org.apache.zookeeper.KeeperException;

public class DeleteResponse extends AsyncResponse {

    private ResponseMetadata metadata;

    public DeleteResponse(KeeperException.Code resultCode, String path, Object ctx, ResponseMetadata metadata) {
        super(resultCode, path, ctx);
        this.metadata = metadata;
    }

    public ResponseMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ResponseMetadata metadata) {
        this.metadata = metadata;
    }
}
