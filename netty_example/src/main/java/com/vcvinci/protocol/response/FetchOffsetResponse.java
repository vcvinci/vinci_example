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
import java.util.Map.Entry;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 下午4:14:16
 * @description 类说明
 */
public class FetchOffsetResponse extends AbstractResponse {

    private static final Schema OFFSET_FETCH_RESPONSE_PARTITION_V0 = new Schema(
            CommonField.PARTITION_ID,
            CommonField.ERROR_CODE,
            CommonField.OFFSET,
            CommonField.METADATA);

    private static final Schema OFFSET_FETCH_RESPONSE_TOPIC_V0 = new Schema(CommonField.TOPIC_NAME, new Field(
            ProtocolSchemaConstant.PARTITIONS_KEY_NAME, new ARRAY<>(OFFSET_FETCH_RESPONSE_PARTITION_V0)));

    private static final Schema OFFSET_FETCH_RESPONSE_V0 = new Schema(
            new Field(ProtocolSchemaConstant.TOPICS_KEY_NAME, new ARRAY<>(OFFSET_FETCH_RESPONSE_TOPIC_V0)));

    private final Map<TopicPartition, OffsetInfo> offsetInfos;

    public static Schema[] schemaVersions() {
        return new Schema[]{OFFSET_FETCH_RESPONSE_V0};
    }

    public FetchOffsetResponse(short version, Map<TopicPartition, OffsetInfo> offsetInfos) {
        super(version);
        this.offsetInfos = offsetInfos;
    }

    public FetchOffsetResponse(Struct struct, short version) {
        super(version);
        offsetInfos = new HashMap<>();
        for (Struct topicResponse : struct.getStructArray(ProtocolSchemaConstant.TOPICS_KEY_NAME)) {
            String topic = topicResponse.get(CommonField.TOPIC_NAME);
            for (Struct partitionResponse : topicResponse.getStructArray(ProtocolSchemaConstant.PARTITIONS_KEY_NAME)) {
                int partition = partitionResponse.get(CommonField.PARTITION_ID);
                ErrorCodes error = ErrorCodes.forCode(partitionResponse.get(CommonField.ERROR_CODE));
                long offset = partitionResponse.get(CommonField.OFFSET);
                String metadata = partitionResponse.get(CommonField.METADATA);
                OffsetInfo offsetInfo = new OffsetInfo(error, offset, metadata);
                offsetInfos.put(new TopicPartition(topic, partition), offsetInfo);
            }
        }
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.FetchOffset.getResponseSchema(getVersion()));
        Map<String, Map<Integer, OffsetInfo>> topicsData = CollectionUtils.groupDataByTopic(offsetInfos);

        List<Struct> topicArray = new ArrayList<>();
        for (Entry<String, Map<Integer, OffsetInfo>> topicEntry : topicsData.entrySet()) {
            Struct topicData = struct.instance(ProtocolSchemaConstant.TOPICS_KEY_NAME);
            topicData.set(CommonField.TOPIC_NAME, topicEntry.getKey());
            List<Struct> partitionArray = new ArrayList<>();
            for (Entry<Integer, OffsetInfo> partitionEntry : topicEntry.getValue().entrySet()) {
                OffsetInfo offsetPartitionData = partitionEntry.getValue();
                Struct partitionData = topicData.instance(ProtocolSchemaConstant.PARTITIONS_KEY_NAME);
                partitionData.set(CommonField.PARTITION_ID, partitionEntry.getKey());
                partitionData.set(CommonField.ERROR_CODE, offsetPartitionData.error.code());
                partitionData.set(CommonField.METADATA, offsetPartitionData.metadata);
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

        Map<ErrorCodes, Integer> errorCounts = new HashMap<>();
        for(Entry<TopicPartition, OffsetInfo> entry: offsetInfos.entrySet()){

            OffsetInfo info = entry.getValue();
            if(info.error == null){
                continue;
            }
            Integer count = errorCounts.get(info.error);
            if(count == null){
                count = 1;
            }else{
                count ++;
            }
            errorCounts.put(info.error, count);
        }
        return errorCounts;
    }

    public boolean hasError(){
        return !this.errorCounts().isEmpty();
    }

    public Map<TopicPartition, OffsetInfo> offsetInfos(){
        return this.offsetInfos;
    }

    public static final class OffsetInfo {

        public static final long INVALID_OFFSET = -1L;
        public final long offset;
        public final String metadata;
        public final ErrorCodes error;

        public OffsetInfo(ErrorCodes error, long offset, String metadata) {
            this.offset = offset;
            this.metadata = metadata;
            this.error = error;
        }

        public boolean hasError() {
            return this.error != ErrorCodes.NONE;
        }
    }

    public static FetchOffsetResponse parse(ByteBuffer buffer, short version) {
        return new FetchOffsetResponse(RequestResponseMapper.FetchOffset.parseResponse(version, buffer), version);
    }

}