package com.vcvinci.protocol.response;

import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.partition.TopicPartition;
import com.vcvinci.common.protocol.Assignment;
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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.vcvinci.common.schema.CommonTypes.INT32;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 下午4:13:11
 * @description 类说明
 */
public class SyncGroupResponse extends AbstractResponse {

    private static final Schema TOPIC_ASSIGNMENT_V0 = new Schema(CommonField.TOPIC_NAME,
            new Field(ProtocolSchemaConstant.PARTITIONS_KEY_NAME, new ARRAY<>(INT32)));

    private static final Schema ASSIGNMENT_V0 = new Schema(
            new Field(ProtocolSchemaConstant.TOPIC_PARTITIONS_KEY_NAME, new ARRAY<>(TOPIC_ASSIGNMENT_V0)));

    private static final Schema SYNC_RESPONSE_V0 = new Schema(CommonField.ERROR_CODE,
            new Field(ProtocolSchemaConstant.MEMBER_ASSIGNMENT_KEY_NAME, ASSIGNMENT_V0));

    public static Schema[] schemaVersions() {
        return new Schema[]{SYNC_RESPONSE_V0};
    }

    private Assignment assignment;

    public SyncGroupResponse(short version, ErrorCodes error, Assignment assignment) {
        super(version, error);
        this.assignment = assignment;
    }

    public SyncGroupResponse(Struct struct, short version) {
        super(version, struct.get(CommonField.ERROR_CODE));

        Struct assignmentStruct = (Struct) struct.get(ProtocolSchemaConstant.MEMBER_ASSIGNMENT_KEY_NAME);

        List<TopicPartition> topicPartitions = new ArrayList<>();
        for (Struct tpStruct : assignmentStruct.getStructArray(ProtocolSchemaConstant.TOPIC_PARTITIONS_KEY_NAME)) {
            String topic = tpStruct.get(CommonField.TOPIC_NAME);
            Integer[] partitions = tpStruct.getIntegerArray(ProtocolSchemaConstant.PARTITIONS_KEY_NAME);

            for (Integer partition : partitions) {
                topicPartitions.add(new TopicPartition(topic, partition));
            }
        }
        this.assignment = new Assignment(topicPartitions);
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.SyncGroup.getResponseSchema(getVersion()));
        struct.set(CommonField.ERROR_CODE, this.error.code());
        Struct assignmentStruct = struct.instance(ProtocolSchemaConstant.MEMBER_ASSIGNMENT_KEY_NAME);

        Map<String, List<Integer>> topicToPartitions = CollectionUtils.groupDataByTopic(this.assignment.getTopicPartitionList());
        List<Struct> topicArray = new ArrayList<>();
        for (Entry<String, List<Integer>> entry : topicToPartitions.entrySet()) {
            Struct tpStruct = assignmentStruct.instance(ProtocolSchemaConstant.TOPIC_PARTITIONS_KEY_NAME);
            tpStruct.set(ProtocolSchemaConstant.TOPIC_KEY_NAME, entry.getKey());
            tpStruct.set(ProtocolSchemaConstant.PARTITIONS_KEY_NAME, entry.getValue().toArray());

            topicArray.add(tpStruct);
        }
        assignmentStruct.set(ProtocolSchemaConstant.TOPIC_PARTITIONS_KEY_NAME, topicArray.toArray());
        struct.set(ProtocolSchemaConstant.MEMBER_ASSIGNMENT_KEY_NAME, assignmentStruct);
        return struct;
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        // TODO Auto-generated method stub
        return null;
    }

    public static SyncGroupResponse parse(ByteBuffer buffer, short version) {
        return new SyncGroupResponse(RequestResponseMapper.SyncGroup.parseResponse(version, buffer), version);
    }

    public Assignment assignment(){
        return this.assignment;
    }

}