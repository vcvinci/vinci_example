package com.vcvinci.protocol.request;

import com.vcvinci.common.partition.TopicPartition;
import com.vcvinci.common.protocol.Assignment;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes.ARRAY;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.response.AbstractResponse;
import com.vcvinci.protocol.util.CollectionUtils;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.vcvinci.common.schema.CommonTypes.INT32;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 上午8:31:36
 * @description 类说明
 */
public class SyncGroupRequest extends AbstractRequest {

    private static final Schema TOPIC_ASSIGNMENT_V0 = new Schema(
            CommonField.TOPIC_NAME,
            new Field(ProtocolSchemaConstant.PARTITIONS_KEY_NAME, new ARRAY<>(INT32)));
    private static final Schema ASSIGNMENT_V0 = new Schema(
            new Field(ProtocolSchemaConstant.TOPIC_PARTITIONS_KEY_NAME, new ARRAY<>(TOPIC_ASSIGNMENT_V0)));

    private static final Schema MEMBER_ASSIGNMENT_V0 = new Schema(CommonField.MEMBER_ID,
            new Field(ProtocolSchemaConstant.MEMBER_ASSIGNMENT_KEY_NAME, ASSIGNMENT_V0));

    private static final Schema SYNC_GROUP_REQUEST_V0 = new Schema(
            CommonField.GROUP_NAME,
            CommonField.GENERATION_ID,
            CommonField.MEMBER_ID,
            new Field(ProtocolSchemaConstant.GROUP_ASSIGNMENT_KEY_NAME, new ARRAY<>(MEMBER_ASSIGNMENT_V0)));

    public static Schema[] schemaVersions() {
        return new Schema[]{SYNC_GROUP_REQUEST_V0};
    }

    private String groupName;
    private String memberId;
    private int generationId;
    private Map<String, Assignment> assignments;

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.SyncGroup.getRequestSchema(getVersion()));
        struct.set(CommonField.GROUP_NAME, groupName);
        struct.set(CommonField.MEMBER_ID, memberId);
        struct.set(CommonField.GENERATION_ID, generationId);
        List<Struct> groupAssignmentList = new ArrayList<>();
        for (Entry<String, Assignment> entry : assignments.entrySet()) {
            Struct groupAssignment = struct.instance(ProtocolSchemaConstant.GROUP_ASSIGNMENT_KEY_NAME);
            groupAssignment.set(CommonField.MEMBER_ID, entry.getKey());

            Struct memberAssignment = groupAssignment.instance(ProtocolSchemaConstant.MEMBER_ASSIGNMENT_KEY_NAME);
            List<TopicPartition> tps = entry.getValue().getTopicPartitionList();
            Map<String, List<Integer>> topicsData = CollectionUtils.groupDataByTopic(tps);
            List<Struct> topicArray = new ArrayList<>();
            for (Entry<String, List<Integer>> entries : topicsData.entrySet()) {
                Struct topicData = memberAssignment.instance(ProtocolSchemaConstant.TOPIC_PARTITIONS_KEY_NAME);
                topicData.set(CommonField.TOPIC_NAME, entries.getKey());
                List<Integer> partitionList = new ArrayList<>(entries.getValue());
                topicData.set(ProtocolSchemaConstant.PARTITIONS_KEY_NAME, partitionList.toArray());
                topicArray.add(topicData);
            }
            memberAssignment.set(ProtocolSchemaConstant.TOPIC_PARTITIONS_KEY_NAME, topicArray.toArray());

            groupAssignment.set(ProtocolSchemaConstant.MEMBER_ASSIGNMENT_KEY_NAME, memberAssignment);
            groupAssignmentList.add(groupAssignment);
        }
        struct.set(ProtocolSchemaConstant.GROUP_ASSIGNMENT_KEY_NAME, groupAssignmentList.toArray());

        return struct;
    }

    public SyncGroupRequest(short version, String groupName, String memberId, int generationId,
                            Map<String, Assignment> assignments) {
        super(RequestResponseMapper.SyncGroup, version);
        this.groupName = groupName;
        this.memberId = memberId;
        this.generationId = generationId;
        this.assignments = assignments;
    }

    public SyncGroupRequest(final Struct struct, final short version) {
        super(RequestResponseMapper.SyncGroup, version);
        this.groupName = struct.get(CommonField.GROUP_NAME);
        this.memberId = struct.get(CommonField.MEMBER_ID);
        this.generationId = struct.get(CommonField.GENERATION_ID);
        assignments = new HashMap<>();

        for (Struct memberData : struct.getStructArray(ProtocolSchemaConstant.GROUP_ASSIGNMENT_KEY_NAME)) {
            String memberId = memberData.get(CommonField.MEMBER_ID);
            Struct assignmentStruct = (Struct) memberData.get(ProtocolSchemaConstant.MEMBER_ASSIGNMENT_KEY_NAME);
            Struct[] topic2PartitionArray = assignmentStruct.getStructArray(ProtocolSchemaConstant.TOPIC_PARTITIONS_KEY_NAME);
            List<TopicPartition> tps = new ArrayList<>();
            for (Struct topic2Partition : topic2PartitionArray) {
                String topic = topic2Partition.get(CommonField.TOPIC_NAME);
                Integer[] partitions = topic2Partition.getIntegerArray(ProtocolSchemaConstant.PARTITIONS_KEY_NAME);
                for (Integer partition : partitions) {
                    TopicPartition tp = new TopicPartition(topic, partition);
                    tps.add(tp);
                }
            }
            assignments.put(memberId, new Assignment(tps));
        }
    }

    @Override
    public AbstractResponse getErrorResponse(int throttleTime, Throwable e) {
        // TODO Auto-generated method stub
        return null;
    }

    public static SyncGroupRequest parse(ByteBuffer buffer, short version) {
        return new SyncGroupRequest(RequestResponseMapper.SyncGroup.parseRequest(version, buffer), version);
    }

    public String groupName() {
        return this.groupName;
    }

    public String memberId() {
        return this.memberId;
    }

    public int getGenerationId() {
        return this.generationId;
    }

    public Map<String, Assignment> getPartitionAssignments() {
        return this.assignments;
    }

    @Override
    public String toString() {
        return "SyncGroupRequest{" +
                "groupName='" + groupName + '\'' +
                ", memberId='" + memberId + '\'' +
                ", generationId=" + generationId +
                ", assignments=" + assignments +
                '}';
    }
}
 