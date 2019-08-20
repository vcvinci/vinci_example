package com.vcvinci.common.util.zookeeper.request;

import org.apache.zookeeper.data.ACL;

import java.util.List;

public class SetAclRequest extends AsyncRequest {

    private List<ACL> acl;

    private int version;

    public SetAclRequest(String path, Object ctx, List<ACL> acl, int version) {
        super(path, ctx);
        this.acl = acl;
        this.version = version;
    }

    public List<ACL> getAcl() {
        return acl;
    }

    public void setAcl(List<ACL> acl) {
        this.acl = acl;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
