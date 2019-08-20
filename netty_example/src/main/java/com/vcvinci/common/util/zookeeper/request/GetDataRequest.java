package com.vcvinci.common.util.zookeeper.request;

public class GetDataRequest extends AsyncRequest {

    public GetDataRequest(String path) {
        super(path, null);
    }

    public GetDataRequest(String path, Object ctx) {
        super(path, ctx);
    }
}
