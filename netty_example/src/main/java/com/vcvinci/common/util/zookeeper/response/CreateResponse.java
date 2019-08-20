package com.vcvinci.common.util.zookeeper.response;

import com.vcvinci.common.util.zookeeper.ResponseMetadata;
import org.apache.zookeeper.KeeperException;

public class CreateResponse extends AsyncResponse {

    private String name;

    private ResponseMetadata metadata;

    public CreateResponse(KeeperException.Code resultCode, String path, Object ctx, String name, ResponseMetadata metadata) {
        super(resultCode, path, ctx);
        this.name = name;
        this.metadata = metadata;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResponseMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ResponseMetadata metadata) {
        this.metadata = metadata;
    }
}
