package com.vcvinci.protocol.response;

import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 下午4:14:44
 * @description 类说明
 */
public class GetGroupInfoResponse extends AbstractResponse {

    private static final Schema GROUP_INFO_V0 = new Schema(
            CommonField.GROUP_NAME,
            new Field(ProtocolSchemaConstant.GROUP_STATE_KEY_NAME, CommonTypes.STRING),
            new Field(ProtocolSchemaConstant.SUPPORTED_PARTITION_ASSIGNOR, CommonTypes.STRING),
            CommonField.GENERATION_ID,
            new Field(ProtocolSchemaConstant.LEADER_KEY_NAME, CommonTypes.STRING)
    );

    private static final Schema GROUPS_DETAIL_RESPONSE_V0 = new Schema(
            CommonField.ERROR_CODE,
            new Field(ProtocolSchemaConstant.GROUP_LIST_KEY, new CommonTypes.ARRAY<>(GROUP_INFO_V0))
    );

    public static Schema[] schemaVersions() {
        return new Schema[]{GROUPS_DETAIL_RESPONSE_V0};
    }

    private final List<GroupInfo> groupInfoList;

    public GetGroupInfoResponse(short version, ErrorCodes error, List<GroupInfo> groupInfoList) {
        super(version, error);
        this.groupInfoList = groupInfoList;
    }

    public GetGroupInfoResponse(Struct struct, short version) {
        super(version, struct.get(CommonField.ERROR_CODE));
        groupInfoList = new ArrayList<>();
        for (Object data : struct.getArray(ProtocolSchemaConstant.GROUP_LIST_KEY)) {
            Struct groupStruct = (Struct) data;
            groupInfoList.add(new GroupInfo(groupStruct.get(CommonField.GROUP_NAME),
                    groupStruct.getString(ProtocolSchemaConstant.GROUP_STATE_KEY_NAME),
                    groupStruct.getString(ProtocolSchemaConstant.SUPPORTED_PARTITION_ASSIGNOR),
                    groupStruct.get(CommonField.GENERATION_ID),
                    groupStruct.getString(ProtocolSchemaConstant.LEADER_KEY_NAME)));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("收到服务端响应的 GetGroupInfoResponse, groupInfoList={}", groupInfoList);
        }
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        return Collections.singletonMap(error, 1);
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.GetGroupInfo.getResponseSchema(getVersion()));
        struct.set(CommonField.ERROR_CODE, error.code());
        List<Struct> structList = new ArrayList<>();
        Struct groupInfoStruct;
        for (GroupInfo groupInfo : groupInfoList) {
            groupInfoStruct = struct.instance(ProtocolSchemaConstant.GROUP_LIST_KEY);
            groupInfoStruct.set(CommonField.GROUP_NAME, groupInfo.groupName);
            groupInfoStruct.set(ProtocolSchemaConstant.GROUP_STATE_KEY_NAME, groupInfo.getState());
            groupInfoStruct.set(ProtocolSchemaConstant.SUPPORTED_PARTITION_ASSIGNOR, groupInfo.getSupportedPartitionAssignor());
            groupInfoStruct.set(CommonField.GENERATION_ID, groupInfo.generationId);
            groupInfoStruct.set(ProtocolSchemaConstant.LEADER_KEY_NAME, groupInfo.getLeader());
            structList.add(groupInfoStruct);
        }
        struct.set(ProtocolSchemaConstant.GROUP_LIST_KEY, structList.toArray());
        return struct;
    }

    public List<GroupInfo> getGroupInfoList() {
        return groupInfoList;
    }

    public static GetGroupInfoResponse parse(ByteBuffer buffer, short version) {
        return new GetGroupInfoResponse(RequestResponseMapper.GetGroupInfo.parseResponse(version, buffer), version);
    }

    public static class GroupInfo {

        private String groupName;
        private String state;
        // 组内第一个成员刚加入且还未触发 InitialDelayedJoinTask 超时任务，此时获取到的这个组这个字段为null；因为第一次赋值是在 触发 InitialDelayedJoinTask 时 -> groupCoordinator -> onCompleteJoin() -> initNextGeneration()
        private String supportedPartitionAssignor;
        // 组内第一个成员刚加入且还未触发 InitialDelayedJoinTask 超时任务，此时获取到的这个组这个字段为 0；因为第一次赋值是在 触发 InitialDelayedJoinTask 时 -> groupCoordinator -> onCompleteJoin() -> initNextGeneration()
        private int generationId;
        private String leader;

        public GroupInfo(String groupName, String state, String supportedPartitionAssignor, int generationId, String leader) {
            this.groupName = groupName;
            this.state = state;
            this.supportedPartitionAssignor = supportedPartitionAssignor;
            this.generationId = generationId;
            this.leader = leader;
        }

        public String getGroupName() {
            return groupName;
        }

        public String getState() {
            return state;
        }

        public String getSupportedPartitionAssignor() {
            return supportedPartitionAssignor;
        }

        public int getGenerationId() {
            return generationId;
        }

        public String getLeader() {
            return leader;
        }

        @Override
        public String toString() {
            return "GroupInfo{" +
                    "groupName='" + groupName + '\'' +
                    ", state='" + state + '\'' +
                    ", supportedPartitionAssignor='" + supportedPartitionAssignor + '\'' +
                    ", generationId=" + generationId +
                    ", leader='" + leader + '\'' +
                    '}';
        }
    }

}