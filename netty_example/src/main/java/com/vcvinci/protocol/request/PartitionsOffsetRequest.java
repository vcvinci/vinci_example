package com.vcvinci.protocol.request;

import com.vcvinci.common.partition.TopicPartition;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.response.AbstractResponse;
import com.vcvinci.protocol.util.CollectionUtils;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.vcvinci.common.util.ProtocolSchemaConstant.TIMESTAMP_KEY_NAME;

public class PartitionsOffsetRequest extends AbstractRequest {

    public static final long EARLIEST_TIMESTAMP = -2L;
    public static final long LATEST_TIMESTAMP = -1L;

    public static final int CONSUMER_REPLICA_ID = -1;
    public static final int DEBUGGING_REPLICA_ID = -2;

    private static final Schema PARTITIONS_OFFSET_REQUEST_PARTITION_V0 = new Schema(
            CommonField.PARTITION_ID,
            CommonField.TIMESTAMP);

    private static final Schema PARTITIONS_OFFSET_REQUEST_TOPIC_V0 = new Schema(
            CommonField.TOPIC_NAME,
            new Field(ProtocolSchemaConstant.PARTITIONS_KEY_NAME, new CommonTypes.ARRAY<>(PARTITIONS_OFFSET_REQUEST_PARTITION_V0), "Partitions to list offset."));

    private static final Schema PARTITIONS_OFFSET_REQUEST_V0 = new Schema(
            CommonField.REPLICA_ID,
            new Field(ProtocolSchemaConstant.TOPICS_KEY_NAME, new CommonTypes.ARRAY<>(PARTITIONS_OFFSET_REQUEST_TOPIC_V0), "Topics to list offsets."));

    private int replicaId;
    private Map<TopicPartition, Long> partitionTimestamps;
    private Set<TopicPartition> duplicatePartitions;

    public PartitionsOffsetRequest(short version, int replicaId, Map<TopicPartition, Long> targetTimes) {
        super(RequestResponseMapper.Pull, version);
        this.replicaId = replicaId;
        this.partitionTimestamps = targetTimes;
        this.duplicatePartitions = Collections.emptySet();
    }

    public static Schema[] schemaVersions() {
        return new Schema[]{PARTITIONS_OFFSET_REQUEST_V0};
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.PartitionsOffset.getRequestSchema(getVersion()));

        Map<String, Map<Integer, Long>> topicsData = CollectionUtils.groupDataByTopic(partitionTimestamps);

        struct.set(ProtocolSchemaConstant.REPLICA_ID_KEY_NAME, replicaId);

        List<Struct> topicArray = new ArrayList<>();
        for (Map.Entry<String, Map<Integer, Long>> topicEntry: topicsData.entrySet()) {
            Struct topicData = struct.instance(ProtocolSchemaConstant.TOPICS_KEY_NAME);
            topicData.set(ProtocolSchemaConstant.TOPIC_NAME_KEY_NAME, topicEntry.getKey());
            List<Struct> partitionArray = new ArrayList<>();
            for (Map.Entry<Integer, Long> partitionEntry : topicEntry.getValue().entrySet()) {
                Long timestamp = partitionEntry.getValue();
                Struct partitionData = topicData.instance(ProtocolSchemaConstant.PARTITIONS_KEY_NAME);
                partitionData.set(ProtocolSchemaConstant.PARTITION_ID_KEY_NAME, partitionEntry.getKey());
                partitionData.set(TIMESTAMP_KEY_NAME, timestamp);
                partitionArray.add(partitionData);
            }
            topicData.set(ProtocolSchemaConstant.PARTITIONS_KEY_NAME, partitionArray.toArray());
            topicArray.add(topicData);
        }
        struct.set(ProtocolSchemaConstant.TOPICS_KEY_NAME, topicArray.toArray());
        return struct;
    }

    public PartitionsOffsetRequest(Struct struct, short version) {
        super(RequestResponseMapper.PartitionsOffset, version);
        Set<TopicPartition> duplicatePartitions = new HashSet<>();
        replicaId = struct.getInt(ProtocolSchemaConstant.REPLICA_ID_KEY_NAME);
        partitionTimestamps = new HashMap<>();
        for (Object topicResponseObj : struct.getArray(ProtocolSchemaConstant.TOPICS_KEY_NAME)) {
            Struct topicResponse = (Struct) topicResponseObj;
            String topic = topicResponse.getString(ProtocolSchemaConstant.TOPIC_NAME_KEY_NAME);
            for (Object partitionResponseObj : topicResponse.getArray(ProtocolSchemaConstant.PARTITIONS_KEY_NAME)) {
                Struct partitionResponse = (Struct) partitionResponseObj;
                int partition = partitionResponse.getInt(ProtocolSchemaConstant.PARTITION_ID_KEY_NAME);
                long timestamp = partitionResponse.getLong(TIMESTAMP_KEY_NAME);
                TopicPartition tp = new TopicPartition(topic, partition);
                if (partitionTimestamps.put(tp, timestamp) != null) {
                    duplicatePartitions.add(tp);
                }
            }
        }
        this.duplicatePartitions = duplicatePartitions;
    }

    @Override
    public AbstractResponse getErrorResponse(int throttleTime, Throwable e) {
        // TODO Auto-generated method stub
        return null;
    }

    public static PartitionsOffsetRequest parse(ByteBuffer buffer, short version) {
        return new PartitionsOffsetRequest(RequestResponseMapper.PartitionsOffset.parseRequest(version, buffer), version);
    }


}
