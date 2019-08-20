package com.vcvinci.protocol.request;

import com.vcvinci.common.partition.TopicPartition;
import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.response.AbstractResponse;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 上午8:11:54
 * @description 类说明
 */
public class StopReplicaRequest extends ControllerRequest {
    private static final Schema STOP_REPLICA_REQUEST_PARTITION_V0 = new Schema(
            new Field(ProtocolSchemaConstant.TOPIC_NAME_KEY_NAME, CommonTypes.STRING, "topic"),
            new Field(ProtocolSchemaConstant.PARTITION_ID_KEY_NAME, CommonTypes.INT32, "partition"));
    private static final Schema STOP_REPLICA_REQUEST_V0 = new Schema(
            new Field(ProtocolSchemaConstant.CONTROLLER_ID_KEY_NAME, CommonTypes.INT32, "The controller id."),
            new Field(ProtocolSchemaConstant.CONTROLLER_EPOCH_KEY_NAME, CommonTypes.INT32, "The controller epoch."),
            new Field(ProtocolSchemaConstant.DELETE_PARTITIONS_KEY_NAME, CommonTypes.BOOLEAN, "Boolean which indicates if replica's partitions must be deleted."),
            new Field(ProtocolSchemaConstant.PARTITIONS_KEY_NAME, new CommonTypes.ARRAY<>(STOP_REPLICA_REQUEST_PARTITION_V0)));

    public static Schema[] schemaVersions() {
        return new Schema[]{STOP_REPLICA_REQUEST_V0};
    }
    private boolean isDelete;
    private Set<TopicPartition> partitions;

    public StopReplicaRequest(int controllerId, int controllerEpoch, short version, boolean isDelete, Set<TopicPartition> partitions) {
        super(controllerId, controllerEpoch, RequestResponseMapper.StopReplica, version);
        this.isDelete = isDelete;
        this.partitions = partitions;
    }

    public StopReplicaRequest(Struct struct, short version) {
        super(struct.getInt(ProtocolSchemaConstant.CONTROLLER_ID_KEY_NAME), struct.getInt(ProtocolSchemaConstant.CONTROLLER_EPOCH_KEY_NAME), RequestResponseMapper.StopReplica, version);
        partitions = new HashSet<>();
        this.isDelete = struct.getBoolean(ProtocolSchemaConstant.DELETE_PARTITIONS_KEY_NAME);
        Object[] partitionsObj = struct.getArray(ProtocolSchemaConstant.PARTITIONS_KEY_NAME);
        for (Object partitionObj : partitionsObj) {
            Struct partitionStruct = (Struct) partitionObj;
            String topic = partitionStruct.getString(ProtocolSchemaConstant.TOPIC_NAME_KEY_NAME);
            int partition = partitionStruct.getInt(ProtocolSchemaConstant.PARTITION_ID_KEY_NAME);
            TopicPartition topicPartition = new TopicPartition(topic, partition);
            partitions.add(topicPartition);
        }
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.StopReplica.getRequestSchema(getVersion()));
        List<Struct> partitionStructs = new ArrayList<>();
        for (TopicPartition partition : partitions) {
            Struct partitionsStrut = new Struct(STOP_REPLICA_REQUEST_PARTITION_V0);
            partitionsStrut.set(ProtocolSchemaConstant.TOPIC_NAME_KEY_NAME, partition.getTopic());
            partitionsStrut.set(ProtocolSchemaConstant.PARTITION_ID_KEY_NAME, partition.getPartition());
            partitionStructs.add(partitionsStrut);
        }
        struct.set(ProtocolSchemaConstant.CONTROLLER_ID_KEY_NAME,controllerId());
        struct.set(ProtocolSchemaConstant.CONTROLLER_EPOCH_KEY_NAME, controllerEpoch());
        struct.set(ProtocolSchemaConstant.DELETE_PARTITIONS_KEY_NAME, isDelete);
        struct.set(ProtocolSchemaConstant.PARTITIONS_KEY_NAME, partitionStructs.toArray());
        return struct;
    }

    @Override
    public AbstractResponse getErrorResponse(int throttleTime, Throwable e) {
        // TODO Auto-generated method stub
        return null;
    }

    public static StopReplicaRequest parse(ByteBuffer buffer, short version) {
        return new StopReplicaRequest(RequestResponseMapper.StopReplica.parseRequest(version, buffer), version);
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public Set<TopicPartition> getPartitions() {
        return partitions;
    }

    public void setPartitions(Set<TopicPartition> partitions) {
        this.partitions = partitions;
    }
}
 