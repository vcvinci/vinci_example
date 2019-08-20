package com.vcvinci.protocol.response.model;

/**
 * @CreateDate: 2018/12/14 15:47
 * @Author: zhengquan.lin@ucarinc.com
 * @Description:
 */
public class GroupErrorCodeMapping {

    private String groupName;
    private short errorCode;

    public GroupErrorCodeMapping(String groupName, short errorCode) {
        this.groupName = groupName;
        this.errorCode = errorCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public short getErrorCode() {
        return errorCode;
    }
}
