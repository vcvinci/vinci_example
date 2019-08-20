package com.vcvinci.protocol.request;

import com.vcvinci.common.partition.TopicPartition;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.response.Response;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhixing.wang E-mail:zhixing.wang@ucarinc.com
 * @version 创建时间：2019/1/16 21:43
 * @description OffsetForLeaderEpochRequest
 */
public class OffsetForLeaderEpochRequest extends AbstractRequest{
    private static final Schema PARTITION_STATES_V0 = new Schema(CommonField.TOPIC_NAME, CommonField.PARTITION_ID, CommonField.LEADER_EPOCH);
    private static final Schema OFFSET_FOR_LEADER_EPOCH_REQUEST_V0 = new Schema(new Field(ProtocolSchemaConstant.PARTITION_STATES_KEY_NAME, new CommonTypes.ARRAY(PARTITION_STATES_V0)));


    private Map<TopicPartition,Integer> partitionsMap;

    public OffsetForLeaderEpochRequest(Map<TopicPartition,Integer> partitionsMap, short version) {
        super(RequestResponseMapper.OffsetsForLeaderEpoch, version);
        this.partitionsMap = partitionsMap;
    }

    public OffsetForLeaderEpochRequest(Struct struct, short version) {
        super(RequestResponseMapper.OffsetsForLeaderEpoch, version);
        Map<TopicPartition,Integer> partitionStates = new HashMap<>();
        for (Object partitionStateObj : struct.getArray(ProtocolSchemaConstant.PARTITION_STATES_KEY_NAME)) {
            Struct partitionStateStruct = (Struct) partitionStateObj;
            String topic = partitionStateStruct.get(CommonField.TOPIC_NAME);
            int partition = partitionStateStruct.get(CommonField.PARTITION_ID);
            int leaderEpoch = partitionStateStruct.get(CommonField.LEADER_EPOCH);
            partitionStates.put(new TopicPartition(topic, partition), leaderEpoch);
        }
        this.partitionsMap = partitionStates;
    }

    @Override
    public Response getErrorResponse(int throttleTime, Throwable e) {
        return null;
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.OffsetsForLeaderEpoch.getRequestSchema(getVersion()));
        List<Struct> partitionStates = new ArrayList<>();
        for (Map.Entry<TopicPartition, Integer> entry : partitionsMap.entrySet()) {
            Struct partitionStruct = new Struct(PARTITION_STATES_V0);
            partitionStruct.set(CommonField.TOPIC_NAME, entry.getKey().getTopic());
            partitionStruct.set(CommonField.PARTITION_ID, entry.getKey().getPartition());
            partitionStruct.set(CommonField.LEADER_EPOCH, entry.getValue());
            partitionStates.add(partitionStruct);
        }
        struct.set(ProtocolSchemaConstant.PARTITION_STATES_KEY_NAME, partitionStates.toArray());
        return struct;
    }

    public static Schema[] schemaVersions() {
        return new Schema[]{OFFSET_FOR_LEADER_EPOCH_REQUEST_V0};
    }

    public Map<TopicPartition, Integer> getPartitionsMap() {
        return partitionsMap;
    }
}
