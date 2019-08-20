package com.vcvinci.protocol.response;

import com.vcvinci.common.Structable;
import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.partition.TopicPartition;
import com.vcvinci.common.record.RecordSet;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.CommonTypes.TypeForSchema;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.schema.Type;
import com.vcvinci.common.util.ProtocolSchemaConstant;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FetchResponse extends AbstractResponse {

    private static final String RECORD_SET_KEY_NAME = "record_set";

    private static final String PARTITIONS_KEY_NAME = "partitions";

    private static final String RESPONSES_KEY_NAME = "responses";

    private static final String PARTITION_DATA_KEY_NAME = "partition_data";

    private static final Schema FETCH_RESPONSE_V0 = new Schema(
            new Field(RESPONSES_KEY_NAME, new CommonTypes.ARRAY<>(TopicPartitions.TOPIC_PARTITIONS_TYPE)));

    private final LinkedHashMap<TopicPartition, PartitionData> responseData;

    public FetchResponse(short version, LinkedHashMap<TopicPartition, PartitionData> responseData) {
        super(version);
        this.responseData = responseData;
    }

    public FetchResponse(Struct struct, short version) {
        super(version);

        if (version == 0) {
            LinkedHashMap<TopicPartition, PartitionData> responseData = new LinkedHashMap<>();
            for (TopicPartitions topicPartitions : (TopicPartitions[]) struct.getArray(RESPONSES_KEY_NAME)) {
                for (PartitionDataPair partitionPair : topicPartitions.getPartitions()) {
                    responseData.put(new TopicPartition(topicPartitions.getTopic(), partitionPair.getPartition()),
                            partitionPair.getPartitionData());
                }
            }

            this.responseData = responseData;
        } else {
            // TODO: checked
            throw new IllegalStateException("unknown version " + version);
        }
    }

    public static FetchResponse parse(ByteBuffer buffer, short version) {
        // 只是为了单测 TODO: 去除这种机制
        return new FetchResponse(FETCH_RESPONSE_V0.read(buffer), version);
    }

    public static Schema[] schemaVersions() {
        return new Schema[] { FETCH_RESPONSE_V0 };
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Struct toStruct() {

        Map<String, List<PartitionDataPair>> groupByTopic = new LinkedHashMap<>();
        for (Entry<TopicPartition, PartitionData> topicPartitionPair : this.responseData.entrySet()) {
            TopicPartition tp = topicPartitionPair.getKey();
            List<PartitionDataPair> partitions = groupByTopic.get(tp.getTopic());
            if (partitions == null) {
                groupByTopic.put(tp.getTopic(), partitions = new ArrayList<>());
            }
            partitions.add(new PartitionDataPair(tp.getPartition(), topicPartitionPair.getValue()));
        }

        List<TopicPartitions> responses = new ArrayList<>(groupByTopic.size());

        for (Entry<String, List<PartitionDataPair>> topicPartitions : groupByTopic.entrySet()) {
            responses.add(new TopicPartitions(topicPartitions.getKey(),
                    topicPartitions.getValue().toArray(new PartitionDataPair[0])));
        }

        Struct struct = new Struct(schemaVersions()[this.getVersion()]);
        struct.set(RESPONSES_KEY_NAME, responses.toArray(new TopicPartitions[0]));
        return struct;
    }

    public LinkedHashMap<TopicPartition, PartitionData> getResponseData() {
        return this.responseData;
    }

    private static class TopicPartitions implements Structable {

        private static final Schema TOPIC_PARTITIONS_V0 = new Schema(CommonField.TOPIC_NAME,
                new Field(PARTITIONS_KEY_NAME, new CommonTypes.ARRAY<>(PartitionDataPair.PARTITION_DATA_PAIR_TYPE)));

        static final Type<TopicPartitions> TOPIC_PARTITIONS_TYPE = new TypeForSchema<TopicPartitions>(
                TOPIC_PARTITIONS_V0) {
        };

        private final Struct struct;

        public TopicPartitions(String topic, PartitionDataPair[] partitions) {
            this.struct = new Struct(TOPIC_PARTITIONS_V0);
            this.struct.set(CommonField.TOPIC_NAME, topic);
            this.struct.set(PARTITIONS_KEY_NAME, partitions);
        }

        public TopicPartitions(Struct struct) {
            this.struct = struct;
        }

        public String getTopic() {
            return this.struct.get(CommonField.TOPIC_NAME);
        }

        public PartitionDataPair[] getPartitions() {
            return (PartitionDataPair[]) this.struct.get(PARTITIONS_KEY_NAME);
        }

        @Override
        public Struct toStruct() {
            return this.struct;
        }

        @Override
        public int hashCode() {
            return this.struct.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof TopicPartitions)) {
                return false;
            }
            return this.struct.equals(((TopicPartitions) obj).struct);
        }
    }

    private static class PartitionDataPair implements Structable {

        private static final Schema PARTITION_DATA_PAIR_V0 = new Schema(CommonField.PARTITION_ID,
                new Field("partition_data", PartitionData.PARTITION_DATA_TYPE));

        static final Type<PartitionDataPair> PARTITION_DATA_PAIR_TYPE = new TypeForSchema<PartitionDataPair>(
                PARTITION_DATA_PAIR_V0) {
        };

        private final Struct struct;

        public PartitionDataPair(int partition, PartitionData partitionData) {
            this.struct = new Struct(PARTITION_DATA_PAIR_V0);
            this.struct.set(CommonField.PARTITION_ID, partition);
            this.struct.set(PARTITION_DATA_KEY_NAME, partitionData);
        }

        public PartitionDataPair(Struct struct) {
            this.struct = struct;
        }

        public int getPartition() {
            return this.struct.get(CommonField.PARTITION_ID);
        }

        public PartitionData getPartitionData() {
            return (PartitionData) this.struct.get(PARTITION_DATA_KEY_NAME);
        }

        @Override
        public Struct toStruct() {
            return this.struct;
        }
    }

    public static class PartitionData implements Structable {

        private static final Schema PARTITION_DATA_V0 = new Schema(CommonField.ERROR_CODE,
                new Field(RECORD_SET_KEY_NAME, CommonTypes.RECORD_SET), CommonField.LOG_START_OFFSET,
                CommonField.HIGH_WATERMARK, new CommonField.Int64(ProtocolSchemaConstant.LOG_END_OFFSET_KEY_NAME, ""),
                CommonField.FETCH_OFFSET,
                new CommonField.Int64(ProtocolSchemaConstant.FOLLOWER_LOG_START_OFFSET_KEY_NAME, ""),
                new CommonField.Int64(ProtocolSchemaConstant.FETCH_TIMESTAMP_KEY_NAME, ""));

        static final Type<PartitionData> PARTITION_DATA_TYPE = new TypeForSchema<PartitionData>(PARTITION_DATA_V0) {
        };

        private final Struct struct;

        public PartitionData(ErrorCodes errorCode, RecordSet recordSet, long logStartOffset, long highWatermark,
                long logEndOffset, long fetchOffset, long followerLogStartOffset, long fetchTimestamp) {
            this.struct = new Struct(PARTITION_DATA_V0);
            this.struct.set(CommonField.ERROR_CODE, errorCode.code());
            this.struct.set(RECORD_SET_KEY_NAME, recordSet);
            this.struct.set(CommonField.LOG_START_OFFSET, logStartOffset);
            this.struct.set(CommonField.HIGH_WATERMARK, highWatermark);
            this.struct.set(ProtocolSchemaConstant.LOG_END_OFFSET_KEY_NAME, logEndOffset);
            this.struct.set(CommonField.FETCH_OFFSET, fetchOffset);
            this.struct.set(ProtocolSchemaConstant.FOLLOWER_LOG_START_OFFSET_KEY_NAME, followerLogStartOffset);
            this.struct.set(ProtocolSchemaConstant.FETCH_TIMESTAMP_KEY_NAME, fetchTimestamp);
        }

        public PartitionData(Struct struct) {
            this.struct = struct;
        }

        public ErrorCodes getErrorCode() {
            return ErrorCodes.forCode(this.struct.get(CommonField.ERROR_CODE));
        }

        public long getHighWatermark() {
            return this.struct.get(CommonField.HIGH_WATERMARK);
        }

        public RecordSet getRecordSet() {
            return (RecordSet) this.struct.get(RECORD_SET_KEY_NAME);
        }

        public long getLogStartOffset() {
            return this.struct.get(CommonField.LOG_START_OFFSET);
        }

        public long getLogEndOffset() {
            return (long) this.struct.get(ProtocolSchemaConstant.LOG_END_OFFSET_KEY_NAME);
        }

        public long getFetchOffset() {
            return this.struct.get(CommonField.FETCH_OFFSET);
        }

        public long getFollowerLogStartOffset() {
            return (long) this.struct.get(ProtocolSchemaConstant.FOLLOWER_LOG_START_OFFSET_KEY_NAME);
        }

        public long getFetchTimestamp() {
            return (long) this.struct.get(ProtocolSchemaConstant.FETCH_TIMESTAMP_KEY_NAME);
        }

        @Override
        public Struct toStruct() {
            return this.struct;
        }
    }
}