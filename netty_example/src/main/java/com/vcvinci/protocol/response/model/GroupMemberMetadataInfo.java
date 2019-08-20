package com.vcvinci.protocol.response.model;

import com.vcvinci.common.exception.ErrorCodes;

import java.util.ArrayList;
import java.util.List;

/**
 * @CreateDate: 2018/12/13 14:25
 * @Author: zhengquan.lin@ucarinc.com
 * @Description:
 */
public class GroupMemberMetadataInfo {

    public static final List<MemberMetadataInfo> EMPTY_MEMBER_METADATA_INFO = new ArrayList<>();
    private ErrorCodes code;
    private String groupName;
    private String state;
    private String supportedPartitionAssignor;
    private int generationId;
    private String leader;
    private List<MemberMetadataInfo> memberMetadataInfoList;

    public ErrorCodes getCode() {
        return code;
    }

    public void setCode(ErrorCodes code) {
        this.code = code;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSupportedPartitionAssignor() {
        return supportedPartitionAssignor;
    }

    public void setSupportedPartitionAssignor(String supportedPartitionAssignor) {
        this.supportedPartitionAssignor = supportedPartitionAssignor;
    }

    public int getGenerationId() {
        return generationId;
    }

    public void setGenerationId(int generationId) {
        this.generationId = generationId;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public List<MemberMetadataInfo> getMemberMetadataInfoList() {
        return memberMetadataInfoList;
    }

    public void setMemberMetadataInfoList(List<MemberMetadataInfo> memberMetadataInfoList) {
        this.memberMetadataInfoList = memberMetadataInfoList;
    }

    public static GroupMemberMetadataInfo newErrorInfo(ErrorCodes errorCodes, String groupName) {
        final GroupMemberMetadataInfo item = new GroupMemberMetadataInfo();
        item.setCode(errorCodes);
        item.setGroupName(groupName);
        item.setMemberMetadataInfoList(EMPTY_MEMBER_METADATA_INFO);
        return item;
    }
}
