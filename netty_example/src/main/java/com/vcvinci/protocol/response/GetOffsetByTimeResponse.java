package com.vcvinci.protocol.response;

import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.partition.TopicPartition;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes.ARRAY;
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

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月23日 上午11:32:19
 * @description 类说明
 */
public class GetOffsetByTimeResponse extends AbstractResponse {

    public static final long UNKNOWN_OFFSET = -1L;

    private static final Schema GET_OFFSET_BY_TIME_RESPONSE_PARTITION_V0 = new Schema(CommonField.PARTITION_ID,
            CommonField.ERROR_CODE, CommonField.TIMESTAMP, CommonField.OFFSET);

    private static final Schema GET_OFFSET_BY_TIME_RESPONSE_TOPIC_V0 = new Schema(CommonField.TOPIC_NAME, new Field(
            ProtocolSchemaConstant.PARTITIONS_KEY_NAME, new ARRAY<Struct>(GET_OFFSET_BY_TIME_RESPONSE_PARTITION_V0)));

    private static final Schema GET_OFFSET_BY_TIME_RESPONSE_V0 = new Schema(
            new Field(ProtocolSchemaConstant.TOPICS_KEY_NAME, new ARRAY<Struct>(GET_OFFSET_BY_TIME_RESPONSE_TOPIC_V0)));

    private Map<TopicPartition, OffsetInfo> offsetInfos;

    public static Schema[] schemaVersions() {
        return new Schema[]{GET_OFFSET_BY_TIME_RESPONSE_V0};
    }

    public GetOffsetByTimeResponse(short version, Map<TopicPartition, OffsetInfo> offsetInfos) {
        super(version);
        this.offsetInfos = offsetInfos;
    }

    public GetOffsetByTimeResponse(Struct struct, short version) {
        super(version);
        offsetInfos = new HashMap<>();
        for (Struct topicResponse : struct.getStructArray(ProtocolSchemaConstant.TOPICS_KEY_NAME)) {
            String topic = topicResponse.get(CommonField.TOPIC_NAME);
            for (Struct partitionResponse : topicResponse.getStructArray(ProtocolSchemaConstant.PARTITIONS_KEY_NAME)) {
                int partition = partitionResponse.get(CommonField.PARTITION_ID);
                ErrorCodes error = ErrorCodes.forCode(partitionResponse.get(CommonField.ERROR_CODE));
                long timestamp = partitionResponse.get(CommonField.TIMESTAMP);
                long offset = partitionResponse.get(CommonField.OFFSET);
                OffsetInfo offsetInfo = new OffsetInfo(error, offset, timestamp);
                offsetInfos.put(new TopicPartition(topic, partition), offsetInfo);
            }
        }
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.GetOffsetByTime.getResponseSchema(getVersion()));
        Map<String, Map<Integer, OffsetInfo>> topicsData = CollectionUtils.groupDataByTopic(offsetInfos);

        List<Struct> topicArray = new ArrayList<>();
        for (Map.Entry<String, Map<Integer, OffsetInfo>> topicEntry : topicsData.entrySet()) {
            Struct topicData = struct.instance(ProtocolSchemaConstant.TOPICS_KEY_NAME);
            topicData.set(CommonField.TOPIC_NAME, topicEntry.getKey());
            List<Struct> partitionArray = new ArrayList<>();
            for (Map.Entry<Integer, OffsetInfo> partitionEntry : topicEntry.getValue().entrySet()) {
                OffsetInfo offsetPartitionData = partitionEntry.getValue();
                Struct partitionData = topicData.instance(ProtocolSchemaConstant.PARTITIONS_KEY_NAME);
                partitionData.set(CommonField.PARTITION_ID, partitionEntry.getKey());
                partitionData.set(CommonField.ERROR_CODE, offsetPartitionData.error.code());
                partitionData.set(CommonField.TIMESTAMP, offsetPartitionData.timestamp);
                partitionData.set(CommonField.OFFSET, offsetPartitionData.offset);
                partitionArray.add(partitionData);
            }
            topicData.set(ProtocolSchemaConstant.PARTITIONS_KEY_NAME, partitionArray.toArray());
            topicArray.add(topicData);
        }
        struct.set(ProtocolSchemaConstant.TOPICS_KEY_NAME, topicArray.toArray());

        return struct;
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        // TODO Auto-generated method stub
        return null;
    }

    public Map<TopicPartition, OffsetInfo> getOffsetInfos() {
        return this.offsetInfos;
    }

    public static GetOffsetByTimeResponse parse(ByteBuffer buffer, short version) {
        return new GetOffsetByTimeResponse(RequestResponseMapper.GetOffsetByTime.parseResponse(version, buffer), version);
    }

    public static class OffsetInfo {

        private ErrorCodes error;
        private Long timestamp;
        private Long offset;

        public OffsetInfo(ErrorCodes error, long offset, long timestamp) {
            this.error = error;
            this.offset = offset;
            this.timestamp = timestamp;
        }

        public ErrorCodes getError() {
            return error;
        }

        public void setError(ErrorCodes error) {
            this.error = error;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public Long getOffset() {
            return offset;
        }

        public void setOffset(Long offset) {
            this.offset = offset;
        }
    }

}
