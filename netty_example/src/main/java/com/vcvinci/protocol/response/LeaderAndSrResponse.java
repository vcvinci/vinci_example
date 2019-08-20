package com.vcvinci.protocol.response;

import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.partition.TopicPartition;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vcvinci.common.schema.CommonField.ERROR_CODE;
import static com.vcvinci.common.schema.CommonField.PARTITION_ID;
import static com.vcvinci.common.schema.CommonField.TOPIC_NAME;
import static com.vcvinci.common.util.ProtocolSchemaConstant.PARTITIONS_KEY_NAME;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 下午4:14:31
 * @description 类说明
 */
public class LeaderAndSrResponse extends AbstractResponse {

    private static final Schema PARTITION_V0 = new Schema(
            TOPIC_NAME,
            PARTITION_ID,
            ERROR_CODE);

    private static final Schema LEADER_AND_ISR_RESPONSE_V0 = new Schema(
            ERROR_CODE,
            new Field(PARTITIONS_KEY_NAME, new CommonTypes.ARRAY<>(PARTITION_V0)));

    public static Schema[] schemaVersions() {
        return new Schema[]{LEADER_AND_ISR_RESPONSE_V0};
    }

    private final Map<TopicPartition, ErrorCodes> partitionErrorCodesMap;

    public LeaderAndSrResponse(short version, ErrorCodes error, Map<TopicPartition, ErrorCodes> partitionErrorCodesMap) {
        super(version, error);
        this.partitionErrorCodesMap = partitionErrorCodesMap;
    }

    public LeaderAndSrResponse(Struct struct, short version) {
        super(version, struct.get(CommonField.ERROR_CODE));
        this.partitionErrorCodesMap = new HashMap<>();
        TopicPartition topicPartition;
        Struct structData;
        for (Object object : struct.getArray(PARTITIONS_KEY_NAME)) {
            structData = (Struct) object;
            topicPartition = new TopicPartition(structData.get(TOPIC_NAME), structData.get(PARTITION_ID));
            partitionErrorCodesMap.put(topicPartition, ErrorCodes.forCode(structData.get(ERROR_CODE)));
        }
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.LeaderAndSr.getResponseSchema(getVersion()));
        List<Struct> dataList = new ArrayList<>(partitionErrorCodesMap.size());
        Struct structObject;
        for (Map.Entry<TopicPartition, ErrorCodes> entry : partitionErrorCodesMap.entrySet()) {
            structObject = struct.instance(PARTITIONS_KEY_NAME);
            structObject.set(TOPIC_NAME, entry.getKey().getTopic());
            structObject.set(PARTITION_ID, entry.getKey().getPartition());
            structObject.set(ERROR_CODE, entry.getValue().code());
            dataList.add(structObject);
        }
        struct.set(PARTITIONS_KEY_NAME, dataList.toArray());
        struct.set(ERROR_CODE, error.code());
        return struct;
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        // TODO Auto-generated method stub
        return null;
    }

    public static LeaderAndSrResponse parse(ByteBuffer buffer, short version) {
        return new LeaderAndSrResponse(RequestResponseMapper.LeaderAndSr.parseResponse(version, buffer), version);
    }

}