package com.vcvinci.protocol.response;

import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.protocol.Subscription;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.CommonTypes.ARRAY;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 下午4:12:53
 * @description 类说明
 */
public class JoinGroupResponse extends AbstractResponse {

    private static final Schema SUBSCRIPTION_V0 = new Schema(CommonField.MEMBER_ID,
            new Field(ProtocolSchemaConstant.TOPICS_KEY_NAME, new ARRAY<>(CommonTypes.STRING)));

    private static final Schema JOIN_GROUP_RESPONSE_V0 = new Schema(
            CommonField.ERROR_CODE,
            CommonField.GENERATION_ID,
            CommonField.MEMBER_ID,
            new Field(ProtocolSchemaConstant.LEADER_KEY_NAME, CommonTypes.STRING),
            new Field(ProtocolSchemaConstant.SUPPORTED_PARTITION_ASSIGNOR, CommonTypes.STRING),
            new Field(ProtocolSchemaConstant.GROUP_MEMBERS, new ARRAY<>(SUBSCRIPTION_V0)));

    public static Schema[] schemaVersions() {
        return new Schema[]{JOIN_GROUP_RESPONSE_V0};
    }

    private int generationId;
    private String memberId;
    private String leaderId;
    private String supportedPartitionAssignor;
    private Map<String, Subscription> members;

    public JoinGroupResponse(short version, ErrorCodes error, int generationId, String memberId, String leaderId,
                             String supportedPartitionAssignor, Map<String, Subscription> members) {
        super(version, error);
        this.generationId = generationId;
        this.memberId = memberId;
        this.leaderId = leaderId;
        this.supportedPartitionAssignor = supportedPartitionAssignor;
        this.members = members;
    }

    public JoinGroupResponse(final Struct struct, final short version) {
        super(version, struct.get(CommonField.ERROR_CODE));
        this.generationId = struct.get(CommonField.GENERATION_ID);
        this.memberId = struct.getString(ProtocolSchemaConstant.MEMBER_ID_KEY_NAME);
        this.leaderId = struct.getString(ProtocolSchemaConstant.LEADER_KEY_NAME);
        this.supportedPartitionAssignor = struct.getString(ProtocolSchemaConstant.SUPPORTED_PARTITION_ASSIGNOR);

        Map<String, Subscription> members = new HashMap<>();
        for (Struct subscribeStruct : struct.getStructArray(ProtocolSchemaConstant.GROUP_MEMBERS)) {
            String memberId = subscribeStruct.getString(ProtocolSchemaConstant.MEMBER_ID_KEY_NAME);
            List<String> topics = Arrays.asList(subscribeStruct.getStringArray(ProtocolSchemaConstant.TOPICS_KEY_NAME));
            members.put(memberId, new Subscription(topics));
        }

        this.members = members;
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.JoinGroup.getResponseSchema(getVersion()));
        struct.set(CommonField.ERROR_CODE, error.code());
        struct.set(CommonField.GENERATION_ID, this.generationId);
        struct.set(ProtocolSchemaConstant.MEMBER_ID_KEY_NAME, this.memberId);
        struct.set(ProtocolSchemaConstant.LEADER_KEY_NAME, this.leaderId);
        struct.set(ProtocolSchemaConstant.SUPPORTED_PARTITION_ASSIGNOR, this.supportedPartitionAssignor);

        List<Struct> memberStructList = new ArrayList<>(members.size());
        for (Entry<String, Subscription> entry : this.members.entrySet()) {
            Struct memberStruct = struct.instance(ProtocolSchemaConstant.GROUP_MEMBERS);
            memberStruct.set(ProtocolSchemaConstant.MEMBER_ID_KEY_NAME, entry.getKey());
            Subscription subscription = entry.getValue();
            memberStruct.set(ProtocolSchemaConstant.TOPICS_KEY_NAME, subscription.topics().toArray());
            memberStructList.add(memberStruct);
        }

        struct.set(ProtocolSchemaConstant.GROUP_MEMBERS, memberStructList.toArray());

        return struct;
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isLeader() {
        return memberId.equals(leaderId);
    }

    public int generation(){
        return this.generationId;
    }

    public String memeberId(){
        return this.memberId;
    }

    public String leader(){
        return this.leaderId;
    }

    public String protocol(){
        return this.supportedPartitionAssignor;
    }

    public Map<String, Subscription> members(){
        return this.members;
    }

    public static JoinGroupResponse parse(ByteBuffer buffer, short version) {
        return new JoinGroupResponse(RequestResponseMapper.JoinGroup.parseResponse(version, buffer), version);
    }

}