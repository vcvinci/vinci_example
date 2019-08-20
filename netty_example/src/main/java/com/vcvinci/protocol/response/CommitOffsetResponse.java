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
 * @version 创建时间：2018年5月4日 下午4:13:56
 * @description 提交offset响应
 */
public class CommitOffsetResponse extends AbstractResponse {

    private static final Schema COMMIT_PARTITION_V0 = new Schema(
            CommonField.PARTITION_ID,
            CommonField.ERROR_CODE);

    private static final Schema COMMIT_TOPIC_V0 = new Schema(
            CommonField.TOPIC_NAME,
            new Field(ProtocolSchemaConstant.PARTITION_DATA, new ARRAY<>(COMMIT_PARTITION_V0)));

    private static final Schema COMMIT_OFFSET_RESPONSE_V0 = new Schema(
            new Field(ProtocolSchemaConstant.RESPONSE_DATA, new ARRAY<>(COMMIT_TOPIC_V0)));

    public static Schema[] schemaVersions() {
        return new Schema[]{COMMIT_OFFSET_RESPONSE_V0};
    }

    private Map<TopicPartition, ErrorCodes> responseData;

    public CommitOffsetResponse(short version, Map<TopicPartition, ErrorCodes> responseData) {
        super(version);
        this.responseData = responseData;
    }

    public CommitOffsetResponse(Struct struct, short version) {
        super(version);
        responseData = new HashMap<>();
        Struct[] topicStructs = struct.getStructArray(ProtocolSchemaConstant.RESPONSE_DATA);
        for (Struct topicStruct : topicStructs) {
            String topic = topicStruct.getString(ProtocolSchemaConstant.TOPIC_KEY_NAME);
            Struct[] partitionStructs = topicStruct.getStructArray(ProtocolSchemaConstant.PARTITION_DATA);
            for (Struct partitionStruct : partitionStructs) {
                int partition = partitionStruct.getInt(ProtocolSchemaConstant.PARTITION_KEY_NAME);
                TopicPartition tp = new TopicPartition(topic, partition);
                error = ErrorCodes.forCode(partitionStruct.get(CommonField.ERROR_CODE));
                this.responseData.put(tp, error);
            }
        }
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(COMMIT_OFFSET_RESPONSE_V0);
        Map<String, Map<Integer, ErrorCodes>> responseMap = CollectionUtils.groupDataByTopic(this.responseData);
        List<Struct> topicStructList = new ArrayList<>(responseMap.size());
        for (Entry<String, Map<Integer, ErrorCodes>> entry : responseMap.entrySet()) {
            String topic = entry.getKey();
            Map<Integer, ErrorCodes> partitionMap = entry.getValue();
            Struct topicStruct = struct.instance(ProtocolSchemaConstant.RESPONSE_DATA);
            topicStruct.set(ProtocolSchemaConstant.TOPIC_KEY_NAME, topic);
            List<Struct> partitionStructList = new ArrayList<>();
            for (Entry<Integer, ErrorCodes> partitionEntry : partitionMap.entrySet()) {
                Struct partitionStruct = topicStruct.instance(ProtocolSchemaConstant.PARTITION_DATA);
                partitionStruct.set(ProtocolSchemaConstant.PARTITION_KEY_NAME, partitionEntry.getKey());
                partitionStruct.set(CommonField.ERROR_CODE, partitionEntry.getValue().code());
                partitionStructList.add(partitionStruct);
            }
            topicStruct.set(ProtocolSchemaConstant.PARTITION_DATA, partitionStructList.toArray());
            topicStructList.add(topicStruct);
        }
        struct.set(ProtocolSchemaConstant.RESPONSE_DATA, topicStructList.toArray());
        return struct;
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        // TODO Auto-generated method stub
        return null;
    }

    public Map<TopicPartition, ErrorCodes> responseData(){
        return this.responseData;
    }

    public static CommitOffsetResponse parse(ByteBuffer buffer, short version) {
        return new CommitOffsetResponse(RequestResponseMapper.CommitOffset.parseResponse(version, buffer), version);
    }

}