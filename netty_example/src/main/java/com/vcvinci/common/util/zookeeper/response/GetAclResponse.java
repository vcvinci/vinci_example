package com.vcvinci.common.util.zookeeper.response;

import com.vcvinci.common.util.zookeeper.ResponseMetadata;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public class GetAclResponse extends AsyncResponse {

    private List<ACL> acl;

    private Stat stat;

    private ResponseMetadata metadata;

    public GetAclResponse(KeeperException.Code resultCode, String path, Object ctx, List<ACL> acl, Stat stat, ResponseMetadata metadata) {
        super(resultCode, path, ctx);
        this.acl = acl;
        this.stat = stat;
        this.metadata = metadata;
    }

    public List<ACL> getAcl() {
        return acl;
    }

    public void setAcl(List<ACL> acl) {
        this.acl = acl;
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
