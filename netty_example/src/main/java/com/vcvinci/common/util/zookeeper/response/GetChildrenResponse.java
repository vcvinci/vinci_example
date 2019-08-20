package com.vcvinci.common.util.zookeeper.response;

import com.vcvinci.common.util.zookeeper.ResponseMetadata;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public class GetChildrenResponse extends AsyncResponse {

    private List<String> children;

    private Stat stat;

    private ResponseMetadata metadata;

    public GetChildrenResponse(KeeperException.Code resultCode, String path, Object ctx, List<String> children, Stat stat, ResponseMetadata metadata) {
        super(resultCode, path, ctx);
        this.children = children;
        this.stat = stat;
        this.metadata = metadata;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
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
