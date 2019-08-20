package com.vcvinci.protocol.response;

import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.partition.TopicPartition;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.util.CollectionUtils;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartitionsOffsetResponse extends AbstractResponse {
    public static final long UNKNOWN_TIMESTAMP = -1L;
    public static final long UNKNOWN_OFFSET = -1L;

    private static final Schema LIST_OFFSET_RESPONSE_PARTITION_V0 = new Schema(
            CommonField.PARTITION_ID,
            CommonField.ERROR_CODE,
            CommonField.TIMESTAMP,
            CommonField.OFFSET);

    private static final Schema LIST_OFFSET_RESPONSE_TOPIC_V0 = new Schema(
            CommonField.TOPIC_NAME,
            new Field(ProtocolSchemaConstant.PARTITIONS_KEY_NAME, new CommonTypes.ARRAY<>(LIST_OFFSET_RESPONSE_PARTITION_V0)));

    private static final Schema LIST_OFFSET_RESPONSE_V0 = new Schema(
            new Field(ProtocolSchemaConstant.RESPONSES_KEY_NAME, new CommonTypes.ARRAY<>(LIST_OFFSET_RESPONSE_TOPIC_V0)));


    public static Schema[] schemaVersions() {
        return new Schema[]{LIST_OFFSET_RESPONSE_V0};
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.PartitionsOffset.getResponseSchema(getVersion()));
        Map<String, Map<Integer, PartitionData>> topicsData = CollectionUtils.groupDataByTopic(responseData);

        List<Struct> topicArray = new ArrayList<>();
        for (Map.Entry<String, Map<Integer, PartitionData>> topicEntry: topicsData.entrySet()) {
            Struct topicData = struct.instance(ProtocolSchemaConstant.RESPONSES_KEY_NAME);
            topicData.set(CommonField.TOPIC_NAME, topicEntry.getKey());
            List<Struct> partitionArray = new ArrayList<>();
            for (Map.Entry<Integer, PartitionData> partitionEntry : topicEntry.getValue().entrySet()) {
                PartitionData offsetPartitionData = partitionEntry.getValue();
                Struct partitionData = topicData.instance(ProtocolSchemaConstant.PARTITIONS_KEY_NAME);
                partitionData.set(ProtocolSchemaConstant.PARTITION_ID_KEY_NAME, partitionEntry.getKey());
                partitionData.set(ProtocolSchemaConstant.ERROR_CODE_KEY_NAME, offsetPartitionData.error.code());
                partitionData.set(ProtocolSchemaConstant.TIMESTAMP_KEY_NAME, offsetPartitionData.timestamp);
                partitionData.set(ProtocolSchemaConstant.OFFSET_KEY_NAME, offsetPartitionData.offset);
                partitionArray.add(partitionData);
            }
            topicData.set(ProtocolSchemaConstant.PARTITIONS_KEY_NAME, partitionArray.toArray());
            topicArray.add(topicData);
        }
        struct.set(ProtocolSchemaConstant.RESPONSES_KEY_NAME, topicArray.toArray());

        return struct;

    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        return null;
    }

    public static PartitionsOffsetResponse parse(ByteBuffer buffer, short version) {
        return new PartitionsOffsetResponse(RequestResponseMapper.PartitionsOffset.parseResponse(version, buffer), version);
    }


    public static final class PartitionData {
        public final ErrorCodes error;
        public final Long timestamp;
        public final Long offset;

        /**
         * Constructor for ListOffsetResponse v0
         */
        @Deprecated
        public PartitionData(ErrorCodes error, List<Long> offsets) {
            this.error = error;
            this.timestamp = null;
            this.offset = null;
        }

        public PartitionData(ErrorCodes error, long timestamp, long offset) {
            this.error = error;
            this.timestamp = timestamp;
            this.offset = offset;
        }

        public Long getOffset() {
            return offset;
        }

        @Override
        public String toString() {
            return "PartitionData{" +
                    "errorCode: " + (int) error.code() +
                    ", timestamp: " + timestamp +
                    ", offset: " + offset +
                    ", offsets: " +
                    "}";
        }
    }

    private Map<TopicPartition, PartitionData> responseData;

    public PartitionsOffsetResponse(short version, short errorCode,  Map<TopicPartition, PartitionData> responseData) {
        super(version, errorCode);
        this.responseData = responseData;
    }

    public Map<TopicPartition, PartitionData> getResponseData() {
        return responseData;
    }
    public PartitionsOffsetResponse(Struct struct, short version) {
        super(version);
        responseData = new HashMap<>();
        for (Object topicResponseObj : struct.getArray(ProtocolSchemaConstant.RESPONSES_KEY_NAME)) {
            Struct topicResponse = (Struct) topicResponseObj;
            String topic = topicResponse.getString(ProtocolSchemaConstant.TOPIC_NAME_KEY_NAME);
            for (Object partitionResponseObj : topicResponse.getArray(ProtocolSchemaConstant.PARTITIONS_KEY_NAME)) {
                Struct partitionResponse = (Struct) partitionResponseObj;
                int partition = partitionResponse.getInt(ProtocolSchemaConstant.PARTITION_ID_KEY_NAME);
                PartitionData partitionData;
                long timestamp = partitionResponse.getLong(ProtocolSchemaConstant.TIMESTAMP_KEY_NAME);
                long offset = partitionResponse.getLong(ProtocolSchemaConstant.OFFSET_KEY_NAME);
                partitionData = new PartitionData(getError(), timestamp, offset);
                responseData.put(new TopicPartition(topic, partition), partitionData);
            }
        }
    }
}
