package com.vcvinci.protocol.request;

import com.vcvinci.common.offset.CommitOffsetData;
import com.vcvinci.common.partition.TopicPartition;
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

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 上午8:15:15
 * @description 类说明
 */
public class CommitOffsetRequest extends AbstractRequest {

    private static final Schema COMMIT_PARTITION_V0 = new Schema(
            CommonField.PARTITION_ID,
            CommonField.OFFSET,
            CommonField.METADATA);

    private static final Schema COMMIT_TOPIC_V0 = new Schema(
            CommonField.TOPIC_NAME,
            new Field(ProtocolSchemaConstant.PARTITIONS_KEY_NAME, new ARRAY<>(COMMIT_PARTITION_V0), "Partitions to commit offsets."));

    private static final Schema COMMIT_OFFSET_REQUEST_V0 = new Schema(
            CommonField.GROUP_NAME,
            CommonField.GENERATION_ID,
            CommonField.MEMBER_ID,
            new Field(ProtocolSchemaConstant.TOPICS_KEY_NAME, new ARRAY<>(COMMIT_TOPIC_V0), "Topics to commit offsets."));

    public static Schema[] schemaVersions() {
        return new Schema[]{COMMIT_OFFSET_REQUEST_V0};
    }

    private String groupName;
    private int generationId;
    private String memberId;
    private Map<TopicPartition, CommitOffsetData> offsets;

    public CommitOffsetRequest(short version, String groupName, String memberId, int generationId, Map<TopicPartition, CommitOffsetData> offsets) {
        super(RequestResponseMapper.CommitOffset, version);
        this.groupName = groupName;
        this.memberId = memberId;
        this.generationId = generationId;
        this.offsets = offsets;
    }

    public CommitOffsetRequest(Struct struct, short version) {
        super(RequestResponseMapper.CommitOffset, version);
        this.groupName = struct.get(CommonField.GROUP_NAME);
        this.generationId = struct.get(CommonField.GENERATION_ID);
        this.memberId = struct.get(CommonField.MEMBER_ID);
        Struct[] topicArray = struct.getStructArray(ProtocolSchemaConstant.TOPICS_KEY_NAME);
        offsets = new HashMap<>();
        String topic;
        for (Struct topicStruct : topicArray) {
            topic = topicStruct.get(CommonField.TOPIC_NAME);
            for (Struct partitionDataStruct : topicStruct.getStructArray(ProtocolSchemaConstant.PARTITIONS_KEY_NAME)) {
                int partition = partitionDataStruct.get(CommonField.PARTITION_ID);
                long commitOffset = partitionDataStruct.get(CommonField.OFFSET);
                String metadata = partitionDataStruct.get(CommonField.METADATA);
                CommitOffsetData offsetMetadata = new CommitOffsetData(commitOffset, metadata);
                offsets.put(new TopicPartition(topic, partition), offsetMetadata);
            }
        }
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.CommitOffset.getRequestSchema(getVersion()));
        struct.set(CommonField.GROUP_NAME, this.groupName);
        Map<String, Map<Integer, CommitOffsetData>> topicsData = CollectionUtils.groupDataByTopic(offsets);
        List<Struct> topicArray = new ArrayList<>();
        for (Map.Entry<String, Map<Integer, CommitOffsetData>> topicEntry : topicsData.entrySet()) {
            Struct topicData = struct.instance(ProtocolSchemaConstant.TOPICS_KEY_NAME);
            topicData.set(CommonField.TOPIC_NAME, topicEntry.getKey());
            List<Struct> partitionArray = new ArrayList<>();
            for (Map.Entry<Integer, CommitOffsetData> partitionEntry : topicEntry.getValue().entrySet()) {
                CommitOffsetData offsetMetadata = partitionEntry.getValue();
                Struct partitionData = topicData.instance(ProtocolSchemaConstant.PARTITIONS_KEY_NAME);
                partitionData.set(CommonField.PARTITION_ID, partitionEntry.getKey());
                partitionData.set(CommonField.OFFSET, offsetMetadata.getCommitOffset());
                partitionData.set(CommonField.METADATA, offsetMetadata.getMetadata());
                partitionArray.add(partitionData);
            }
            topicData.set(ProtocolSchemaConstant.PARTITIONS_KEY_NAME, partitionArray.toArray());
            topicArray.add(topicData);
        }
        struct.set(ProtocolSchemaConstant.TOPICS_KEY_NAME, topicArray.toArray());
        struct.set(CommonField.GENERATION_ID, generationId);
        struct.set(CommonField.MEMBER_ID, memberId);
        return struct;
    }

    @Override
    public AbstractResponse getErrorResponse(int throttleTime, Throwable e) {
        // TODO Auto-generated method stub
        return null;
    }

    public static CommitOffsetRequest parse(ByteBuffer buffer, short version) {
        return new CommitOffsetRequest(RequestResponseMapper.CommitOffset.parseRequest(version, buffer), version);
    }

    public String groupName() {
        return groupName;
    }

    public Map<TopicPartition, CommitOffsetData> offsets() {
        return offsets;
    }

    public int generationId() {
        return generationId;
    }

    public String memberId() {
        return memberId;
    }

    @Override
    public String toString() {
        return "CommitOffsetRequest{"
                + "groupName='" + groupName + '\''
                + ", generationId=" + generationId
                + ", memberId='" + memberId + '\''
                + ", offsets=" + offsets
                + '}';
    }
}
