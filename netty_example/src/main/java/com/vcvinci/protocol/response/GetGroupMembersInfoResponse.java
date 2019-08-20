package com.vcvinci.protocol.response;

import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.partition.TopicPartition;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.response.model.GroupMemberMetadataInfo;
import com.vcvinci.protocol.response.model.MemberMetadataInfo;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @CreateDate: 2018/11/12 21:36
 * @Author: zhengquan.lin@ucarinc.com
 * @Description: 获取所有指定组的成员信息
 */
public class GetGroupMembersInfoResponse extends AbstractResponse {

    private static final Schema TOPIC_ASSIGNMENT_V0 = new Schema(
            CommonField.TOPIC_NAME,
            CommonField.PARTITION_ID);

    private static final Schema MEMBER_METADATA_V0 = new Schema(
            CommonField.MEMBER_ID,
            new Field(ProtocolSchemaConstant.CLIENT_HOST_KEY, CommonTypes.STRING),
            new Field(ProtocolSchemaConstant.SESSION_TIMEOUT_KEY_NAME, CommonTypes.INT32),
            new Field(ProtocolSchemaConstant.REBALANCE_TIMEOUT_KEY_NAME, CommonTypes.INT32),
            new Field(ProtocolSchemaConstant.TOPICS_KEY_NAME, new CommonTypes.ARRAY<>(CommonTypes.STRING)),
            new Field(ProtocolSchemaConstant.MEMBER_ASSIGNMENT_KEY_NAME, new CommonTypes.ARRAY<>(TOPIC_ASSIGNMENT_V0))
    );

    private static final Schema GROUP_METADATA_V0 = new Schema(
            CommonField.ERROR_CODE,
            CommonField.GROUP_NAME,
            new Field(ProtocolSchemaConstant.GROUP_STATE_KEY_NAME, CommonTypes.NULLABLE_STRING),
            new Field(ProtocolSchemaConstant.SUPPORTED_PARTITION_ASSIGNOR, CommonTypes.NULLABLE_STRING),
            new Field(ProtocolSchemaConstant.GENERATION_ID_KEY_NAME, CommonTypes.INT32),
            new Field(ProtocolSchemaConstant.LEADER_KEY_NAME, CommonTypes.NULLABLE_STRING),
            new Field(ProtocolSchemaConstant.GROUP_MEMBERS, new CommonTypes.ARRAY<>(MEMBER_METADATA_V0))
    );

    private static final Schema GET_GROUP_MEMBERS_INFO_RESPONSE_V0 = new Schema(
            new Field(ProtocolSchemaConstant.GROUP_LIST_KEY, new CommonTypes.ARRAY<>(GROUP_METADATA_V0))
    );

    public static Schema[] schemaVersions() {
        return new Schema[]{GET_GROUP_MEMBERS_INFO_RESPONSE_V0};
    }

    private final Map<String, GroupMemberMetadataInfo> groupMemberMetadataInfoMap;

    public GetGroupMembersInfoResponse(short version, Map<String, GroupMemberMetadataInfo> groupMemberMetadataInfoMap) {
        this(version, ErrorCodes.NONE, groupMemberMetadataInfoMap);
    }

    public GetGroupMembersInfoResponse(short version, ErrorCodes error, Map<String, GroupMemberMetadataInfo> groupMemberMetadataInfoMap) {
        super(version, error);
        this.groupMemberMetadataInfoMap = groupMemberMetadataInfoMap;
    }

    public GetGroupMembersInfoResponse(Struct struct, short version) {
        super(version);
        groupMemberMetadataInfoMap = new HashMap<>();
        GroupMemberMetadataInfo groupMemberMetadataInfo;
        for (Object data : struct.getArray(ProtocolSchemaConstant.GROUP_LIST_KEY)) {
            Struct groupMetadataStruct = (Struct) data;
            groupMemberMetadataInfo = new GroupMemberMetadataInfo();
            groupMemberMetadataInfo.setCode(ErrorCodes.forCode(groupMetadataStruct.get(CommonField.ERROR_CODE)));
            groupMemberMetadataInfo.setGroupName(groupMetadataStruct.getString(ProtocolSchemaConstant.GROUP_KEY_NAME));
            groupMemberMetadataInfo.setGenerationId(groupMetadataStruct.get(CommonField.GENERATION_ID));
            groupMemberMetadataInfo.setLeader(groupMetadataStruct.getString(ProtocolSchemaConstant.LEADER_KEY_NAME));
            groupMemberMetadataInfo.setSupportedPartitionAssignor(groupMetadataStruct.getString(ProtocolSchemaConstant.SUPPORTED_PARTITION_ASSIGNOR));
            groupMemberMetadataInfo.setState(groupMetadataStruct.getString(ProtocolSchemaConstant.GROUP_STATE_KEY_NAME));

            List<MemberMetadataInfo> memberMetadataInfoList = new ArrayList<>();
            MemberMetadataInfo memberMetadataInfo;
            for (Object memberData : groupMetadataStruct.getArray(ProtocolSchemaConstant.GROUP_MEMBERS)) {
                Struct memberStruct = (Struct) memberData;
                List<TopicPartition> memberAssignmentList = new ArrayList<>();
                TopicPartition topicAssignment;
                for (Object tpData : memberStruct.getArray(ProtocolSchemaConstant.MEMBER_ASSIGNMENT_KEY_NAME)) {
                    Struct tpStruct = (Struct) tpData;
                    topicAssignment = new TopicPartition(tpStruct.get(CommonField.TOPIC_NAME), tpStruct.get(CommonField.PARTITION_ID));
                    memberAssignmentList.add(topicAssignment);
                }
                memberMetadataInfo = new MemberMetadataInfo(memberStruct.get(CommonField.MEMBER_ID),
                        memberStruct.getString(ProtocolSchemaConstant.CLIENT_HOST_KEY),
                        memberStruct.getInt(ProtocolSchemaConstant.SESSION_TIMEOUT_KEY_NAME),
                        memberStruct.getInt(ProtocolSchemaConstant.REBALANCE_TIMEOUT_KEY_NAME), Arrays.asList(memberStruct.getStringArray(ProtocolSchemaConstant.TOPICS_KEY_NAME)),
                        memberAssignmentList);
                memberMetadataInfoList.add(memberMetadataInfo);
            }
            groupMemberMetadataInfo.setMemberMetadataInfoList(memberMetadataInfoList);
            groupMemberMetadataInfoMap.put(groupMemberMetadataInfo.getGroupName(), groupMemberMetadataInfo);
        }
    }

    @Override
    public Struct toStruct() {
        final Struct struct = new Struct(RequestResponseMapper.GetGroupMembersInfo.getResponseSchema(getVersion()));
        final List<Struct> groupMetadataList = new ArrayList<>();
        Struct groupMetadataStruct;
        GroupMemberMetadataInfo groupMemberMetadataInfo;
        for (Map.Entry<String, GroupMemberMetadataInfo> item : groupMemberMetadataInfoMap.entrySet()) {
            groupMetadataStruct = struct.instance(ProtocolSchemaConstant.GROUP_LIST_KEY);
            groupMemberMetadataInfo = item.getValue();
            groupMetadataStruct.set(CommonField.ERROR_CODE, groupMemberMetadataInfo.getCode().code());
            groupMetadataStruct.set(ProtocolSchemaConstant.GROUP_KEY_NAME, groupMemberMetadataInfo.getGroupName());
            groupMetadataStruct.set(ProtocolSchemaConstant.GROUP_STATE_KEY_NAME, groupMemberMetadataInfo.getState());
            groupMetadataStruct.set(ProtocolSchemaConstant.SUPPORTED_PARTITION_ASSIGNOR, groupMemberMetadataInfo.getSupportedPartitionAssignor());
            groupMetadataStruct.set(CommonField.GENERATION_ID, groupMemberMetadataInfo.getGenerationId());
            groupMetadataStruct.set(ProtocolSchemaConstant.LEADER_KEY_NAME, groupMemberMetadataInfo.getLeader());

            final List<Struct> memberStructList = new ArrayList<>();
            Struct memberStruct;
            List<Struct> topicAssignmentList;
            Struct taStruct;
            for (MemberMetadataInfo memberMetadataInfo : groupMemberMetadataInfo.getMemberMetadataInfoList()) {
                memberStruct = groupMetadataStruct.instance(ProtocolSchemaConstant.GROUP_MEMBERS);
                memberStruct.set(CommonField.MEMBER_ID, memberMetadataInfo.getMemberId());
                memberStruct.set(ProtocolSchemaConstant.CLIENT_HOST_KEY, memberMetadataInfo.getClientHost());
                memberStruct.set(ProtocolSchemaConstant.SESSION_TIMEOUT_KEY_NAME, memberMetadataInfo.getSessionTimeout());
                memberStruct.set(ProtocolSchemaConstant.REBALANCE_TIMEOUT_KEY_NAME, memberMetadataInfo.getRebalanceTimeout());
                memberStruct.set(ProtocolSchemaConstant.TOPICS_KEY_NAME, memberMetadataInfo.getTopicList().toArray());
                topicAssignmentList = new ArrayList<>();
                for (TopicPartition ta : memberMetadataInfo.getMemberAssignmentList()) {
                    taStruct = memberStruct.instance(ProtocolSchemaConstant.MEMBER_ASSIGNMENT_KEY_NAME);
                    taStruct.set(CommonField.TOPIC_NAME, ta.getTopic());
                    taStruct.set(CommonField.PARTITION_ID, ta.getPartition());
                    topicAssignmentList.add(taStruct);
                }
                memberStruct.set(ProtocolSchemaConstant.MEMBER_ASSIGNMENT_KEY_NAME, topicAssignmentList.toArray());
                memberStructList.add(memberStruct);
            }
            groupMetadataStruct.set(ProtocolSchemaConstant.GROUP_MEMBERS, memberStructList.toArray());
            groupMetadataList.add(groupMetadataStruct);
        }
        struct.set(ProtocolSchemaConstant.GROUP_LIST_KEY, groupMetadataList.toArray());
        return struct;
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        return null;
    }

    public Map<String, GroupMemberMetadataInfo> getGroupMemberMetadataInfoMap() {
        return groupMemberMetadataInfoMap;
    }

    public static GetGroupMembersInfoResponse parse(ByteBuffer buffer, short version) {
        return new GetGroupMembersInfoResponse(RequestResponseMapper.GetGroupMembersInfo.parseResponse(version, buffer), version);
    }
}
