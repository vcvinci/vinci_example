package com.vcvinci.protocol.response.model;

import com.vcvinci.common.partition.TopicPartition;

import java.util.ArrayList;
import java.util.List;

/**
 * @CreateDate: 2018/12/13 14:23
 * @Author: zhengquan.lin@ucarinc.com
 * @Description:
 */
public class MemberMetadataInfo {

    public static final List<TopicPartition> EMPTY = new ArrayList<>();

    private final String memberId;
    private final String clientHost;
    private final int sessionTimeout;
    private final int rebalanceTimeout;
    private final List<String> topicList;
    private List<TopicPartition> memberAssignmentList;

    public MemberMetadataInfo(String memberId, String clientHost, int sessionTimeout, int rebalanceTimeout, List<String> topicList) {
        this(memberId, clientHost, sessionTimeout, rebalanceTimeout, topicList, EMPTY);
    }

    public MemberMetadataInfo(String memberId, String clientHost, int sessionTimeout, int rebalanceTimeout, List<String> topicList, List<TopicPartition> memberAssignmentList) {
        this.memberId = memberId;
        this.clientHost = clientHost;
        this.sessionTimeout = sessionTimeout;
        this.rebalanceTimeout = rebalanceTimeout;
        this.topicList = topicList;
        this.memberAssignmentList = memberAssignmentList;
    }

    public String getMemberId() {
        return memberId;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public int getRebalanceTimeout() {
        return rebalanceTimeout;
    }

    public List<String> getTopicList() {
        return topicList;
    }

    public String getClientHost() {
        return clientHost;
    }

    public List<TopicPartition> getMemberAssignmentList() {
        return memberAssignmentList;
    }

    public void setMemberAssignmentList(List<TopicPartition> memberAssignmentList) {
        this.memberAssignmentList = memberAssignmentList;
    }

}
