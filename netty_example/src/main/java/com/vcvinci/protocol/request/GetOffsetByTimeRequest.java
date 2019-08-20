package com.vcvinci.protocol.request;

import com.vcvinci.common.partition.TopicPartition;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes.ARRAY;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.response.AbstractResponse;
import com.vcvinci.protocol.util.CollectionUtils;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 上午8:38:20
 * @description 类说明
 */
public class GetOffsetByTimeRequest extends AbstractRequest {

    private Map<TopicPartition, Long> timestamps;

    private static final Schema PARTITION_TO_TIMESTAMP = new Schema(CommonField.PARTITION_ID, CommonField.TIMESTAMP);

    private static final Schema TOPIC_TO_PARTITIONS = new Schema(CommonField.TOPIC_NAME,
            new Field(ProtocolSchemaConstant.PARTITIONS_KEY_NAME, new ARRAY<>(PARTITION_TO_TIMESTAMP),
                    "partitions to get offset by timestamp"));

    private static final Schema GET_OFFSET_BY_TIME_REQUEST_V0 = new Schema(
            new Field(ProtocolSchemaConstant.TOPICS_KEY_NAME, new ARRAY<>(TOPIC_TO_PARTITIONS),
                    "Topics to get offset by timestamp."));

    public GetOffsetByTimeRequest(short version, Map<TopicPartition, Long> timestamps) {
        super(RequestResponseMapper.GetOffsetByTime, version);
        this.timestamps = timestamps;
    }

    public GetOffsetByTimeRequest(short version, Struct struct) {
        super(RequestResponseMapper.GetOffsetByTime, version);
        timestamps = new HashMap<TopicPartition, Long>();
        for (Struct topicResponse : struct.getStructArray(ProtocolSchemaConstant.TOPICS_KEY_NAME)) {
            String topic = topicResponse.get(CommonField.TOPIC_NAME);
            for (Struct partitionResponse : topicResponse.getStructArray(ProtocolSchemaConstant.PARTITIONS_KEY_NAME)) {
                int partition = partitionResponse.get(CommonField.PARTITION_ID);
                long timestamp = partitionResponse.get(CommonField.TIMESTAMP);
                TopicPartition tp = new TopicPartition(topic, partition);
                timestamps.put(tp, timestamp);
            }
        }
    }

    public static Schema[] schemaVersions() {
        return new Schema[]{GET_OFFSET_BY_TIME_REQUEST_V0};
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.GetOffsetByTime.getRequestSchema(getVersion()));
        Map<String, Map<Integer, Long>> topicsData = CollectionUtils.groupDataByTopic(this.timestamps);
        List<Struct> topicArray = new ArrayList<Struct>();
        for (Map.Entry<String, Map<Integer, Long>> topicEntry : topicsData.entrySet()) {
            Struct topicData = struct.instance(ProtocolSchemaConstant.TOPICS_KEY_NAME);
            topicData.set(CommonField.TOPIC_NAME, topicEntry.getKey());
            List<Struct> partitionArray = new ArrayList<>();
            for (Map.Entry<Integer, Long> partitionEntry : topicEntry.getValue().entrySet()) {
                Long timestamp = (Long) partitionEntry.getValue();
                Struct partitionData = topicData.instance(ProtocolSchemaConstant.PARTITIONS_KEY_NAME);
                partitionData.set(CommonField.PARTITION_ID, partitionEntry.getKey());
                partitionData.set(CommonField.TIMESTAMP, timestamp);
                partitionArray.add(partitionData);
            }
            topicData.set(ProtocolSchemaConstant.PARTITIONS_KEY_NAME, partitionArray.toArray());
            topicArray.add(topicData);
        }
        struct.set(ProtocolSchemaConstant.TOPICS_KEY_NAME, topicArray.toArray());
        return struct;
    }

    @Override
    public AbstractResponse getErrorResponse(int throttleTime, Throwable e) {
        // TODO Auto-generated method stub
        return null;
    }

    public static GetOffsetByTimeRequest parse(ByteBuffer buffer, short version) {
        return new GetOffsetByTimeRequest(version, RequestResponseMapper.GetOffsetByTime.parseRequest(version, buffer));
    }
}
