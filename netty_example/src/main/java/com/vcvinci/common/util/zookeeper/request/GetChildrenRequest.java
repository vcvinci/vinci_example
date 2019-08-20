package com.vcvinci.common.util.zookeeper.request;

public class GetChildrenRequest extends AsyncRequest {

    public GetChildrenRequest(String path, Object ctx) {
        super(path, ctx);
    }
}
