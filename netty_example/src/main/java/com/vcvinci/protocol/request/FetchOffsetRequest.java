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
import java.util.List;
import java.util.Map;

import static com.vcvinci.common.schema.CommonTypes.INT32;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 上午8:26:55
 * @description 类说明
 *
 * 获取指定消费组中 指定的topic-Partition的 offset 信息
 * 如果topic为空，那么返回所有 topics
 */
public class FetchOffsetRequest extends AbstractRequest {

    private static final Schema OFFSET_FETCH_REQUEST_TOPIC_V0 = new Schema(
            CommonField.TOPIC_NAME,
            new Field(ProtocolSchemaConstant.PARTITIONS_KEY_NAME, new ARRAY<>(INT32), "Partitions to fetch offsets."));

    private static final Schema OFFSET_FETCH_REQUEST_V0 = new Schema(
            CommonField.GROUP_NAME,
            new Field(ProtocolSchemaConstant.TOPICS_KEY_NAME, new ARRAY<>(OFFSET_FETCH_REQUEST_TOPIC_V0), "Topics to fetch offsets. If the " + "topic array is null fetch offsets for all topics."));

    public static Schema[] schemaVersions() {
        return new Schema[]{OFFSET_FETCH_REQUEST_V0};
    }

    private final String groupName;
    private final List<TopicPartition> tps;

    public FetchOffsetRequest(short version, String groupName, List<TopicPartition> tps) {
        super(RequestResponseMapper.FetchOffset, version);
        this.groupName = groupName;
        this.tps = tps;
    }

    public FetchOffsetRequest(Struct struct, short version) {
        super(RequestResponseMapper.FetchOffset, version);
        this.groupName = struct.get(CommonField.GROUP_NAME);
        Struct[] topicArray = struct.getStructArray(ProtocolSchemaConstant.TOPICS_KEY_NAME);
        tps = new ArrayList<>();
        for (Struct topicStruct : topicArray) {
            String topic = topicStruct.get(CommonField.TOPIC_NAME);
            for (Integer partition : topicStruct.getIntegerArray(ProtocolSchemaConstant.PARTITIONS_KEY_NAME)) {
                tps.add(new TopicPartition(topic, partition));
            }
        }
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.FetchOffset.getRequestSchema(getVersion()));
        struct.set(CommonField.GROUP_NAME, this.groupName);
        Map<String, List<Integer>> topicsData = CollectionUtils.groupDataByTopic(tps);
        List<Struct> topicArray = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entries : topicsData.entrySet()) {
            Struct topicData = struct.instance(ProtocolSchemaConstant.TOPICS_KEY_NAME);
            topicData.set(CommonField.TOPIC_NAME, entries.getKey());
            topicData.set(ProtocolSchemaConstant.PARTITIONS_KEY_NAME, entries.getValue().toArray());
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

    public static FetchOffsetRequest parse(ByteBuffer buffer, short version) {
        return new FetchOffsetRequest(RequestResponseMapper.FetchOffset.parseRequest(version, buffer), version);
    }

    public String groupName() {
        return this.groupName;
    }

    public List<TopicPartition> topicPartitions() {
        return this.tps;
    }
    public boolean isAllPartitions() {
        return tps == null;
    }

    @Override
    public String toString() {
        return "FetchOffsetRequest{"
                + "groupName='" + groupName + '\''
                + ", tps=" + tps
                + '}';
    }
}
