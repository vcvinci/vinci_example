package com.vcvinci.protocol.response;

/**
 * @author zhixing.wang E-mail:zhixing.wang@ucarinc.com
 * @version 创建时间：2019/1/17 18:51
 * @description OffsetForLeaderEpochResponse
 */
import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.partition.TopicPartition;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhixing.wang E-mail:zhixing.wang@ucarinc.com
 * @version 创建时间：2019/1/16 21:55
 * @description OffsetForLeaderEpochResponse
 */
public class OffsetForLeaderEpochResponse extends AbstractResponse {
    private static final Schema PARTITIONS_OFFSET_MAP = new Schema(CommonField.TOPIC_NAME, CommonField.PARTITION_ID, CommonField.OFFSET);
    private static final Schema OFFSET_FOR_LEADER_EPOCH_RESPONSE_V0 = new Schema(new Field(ProtocolSchemaConstant.PARTITION_EPOCH_OFFSET_KEY_NAME, new CommonTypes.ARRAY(PARTITIONS_OFFSET_MAP)));
    private Map<TopicPartition, Long> epochEndOffsetsByPartition;

    public OffsetForLeaderEpochResponse(Map<TopicPartition, Long> epochEndOffsetsByPartition, short version) {
        super(version);
        this.epochEndOffsetsByPartition = epochEndOffsetsByPartition;
    }

    public OffsetForLeaderEpochResponse(Struct struct, short version) {
        super(version);
        Map<TopicPartition, Long> epochEndOffsetsByPartition = new HashMap<>();
        for (Struct partitionsOffsetStruct : struct.getStructArray(ProtocolSchemaConstant.PARTITION_EPOCH_OFFSET_KEY_NAME)) {
            String topic = partitionsOffsetStruct.get(CommonField.TOPIC_NAME);
            int partition = partitionsOffsetStruct.get(CommonField.PARTITION_ID);
            long offset = partitionsOffsetStruct.get(CommonField.OFFSET);
            epochEndOffsetsByPartition.put(new TopicPartition(topic, partition), offset);
        }
        this.epochEndOffsetsByPartition = epochEndOffsetsByPartition;
    }

    public static Schema[] schemaVersions() {
        return new Schema[]{OFFSET_FOR_LEADER_EPOCH_RESPONSE_V0};
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        return null;
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.OffsetsForLeaderEpoch.getResponseSchema(getVersion()));
        List<Struct> partitionStates = new ArrayList<>();
        for (Map.Entry<TopicPartition, Long> entry : epochEndOffsetsByPartition.entrySet()) {
            Struct partitionStruct = new Struct(PARTITIONS_OFFSET_MAP);
            partitionStruct.set(CommonField.TOPIC_NAME, entry.getKey().getTopic());
            partitionStruct.set(CommonField.PARTITION_ID, entry.getKey().getPartition());
            partitionStruct.set(CommonField.OFFSET, entry.getValue());
            partitionStates.add(partitionStruct);
        }
        struct.set(ProtocolSchemaConstant.PARTITION_EPOCH_OFFSET_KEY_NAME, partitionStates.toArray());
        return struct;
    }

    public Map<TopicPartition, Long> getEpochEndOffsetsByPartition() {
        return epochEndOffsetsByPartition;
    }
}