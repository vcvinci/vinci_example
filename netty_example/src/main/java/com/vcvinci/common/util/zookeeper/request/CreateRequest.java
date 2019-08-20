package com.vcvinci.common.util.zookeeper.request;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.ACL;

import java.util.List;

public class CreateRequest extends AsyncRequest {

    private byte[] data;

    private List<ACL> acl;

    private CreateMode createMode;

    public CreateRequest(String path, byte[] data, List<ACL> acl, CreateMode createMode) {
        super(path, null);
        this.data = data;
        this.acl = acl;
        this.createMode = createMode;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public List<ACL> getAcl() {
        return acl;
    }

    public void setAcl(List<ACL> acl) {
        this.acl = acl;
    }

    public CreateMode getCreateMode() {
        return createMode;
    }

    public void setCreateMode(CreateMode createMode) {
        this.createMode = createMode;
    }
}
