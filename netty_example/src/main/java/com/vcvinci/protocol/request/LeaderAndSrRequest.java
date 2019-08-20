package com.vcvinci.protocol.request;

import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.partition.PartitionState;
import com.vcvinci.common.partition.TopicPartition;
import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.response.LeaderAndSrResponse;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vcvinci.common.schema.CommonTypes.BOOLEAN;
import static com.vcvinci.common.schema.CommonTypes.INT32;
import static com.vcvinci.common.schema.CommonTypes.STRING;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 上午7:32:51
 * @description 类说明
 */
public class LeaderAndSrRequest extends ControllerRequest {

    private static final Schema LEADER_AND_SR_REQUEST_PARTITION_STATE_V0 = new Schema(
            new Field(ProtocolSchemaConstant.TOPIC_KEY_NAME, STRING, "topic"),
            new Field(ProtocolSchemaConstant.PARTITION_KEY_NAME, INT32, "partition"),
            new Field(ProtocolSchemaConstant.CONTROLLER_EPOCH_KEY_NAME, INT32, "The controller epoch."),
            new Field(ProtocolSchemaConstant.LEADER_KEY_NAME, INT32, "The broker id for the leader."),
            new Field(ProtocolSchemaConstant.LEADER_EPOCH, INT32, "The leader epoch."),
            new Field(ProtocolSchemaConstant.SR_KEY_NAME, new CommonTypes.ARRAY(INT32), "The in sync replica ids."),
            new Field(ProtocolSchemaConstant.REPLICAS_KEY_NAME, new CommonTypes.ARRAY<>(INT32), "The replica ids."),
            new Field(ProtocolSchemaConstant.IS_NEW_KEY_NAME, BOOLEAN, "Whether the replica should have existed on the broker or not"),
            new Field(ProtocolSchemaConstant.OFFLINE_REPLICAS_KEY_NAME, new CommonTypes.ARRAY<>(INT32), "offline replica"));

    private static final Schema LEADER_AND_SR_REQUEST_V0 = new Schema(
            new Field(ProtocolSchemaConstant.CONTROLLER_ID_KEY_NAME, INT32, "The controller id."),
            new Field(ProtocolSchemaConstant.CONTROLLER_EPOCH_KEY_NAME, INT32, "The controller epoch."),
            new Field(ProtocolSchemaConstant.PARTITION_STATES_KEY_NAME, new CommonTypes.ARRAY<>(LEADER_AND_SR_REQUEST_PARTITION_STATE_V0)));

    private Map<TopicPartition, PartitionState> partitionStates;

    public LeaderAndSrRequest(int controllerId, int controllerEpoch, Map<TopicPartition, PartitionState> partitionStates, short version) {
        super(controllerId, controllerEpoch, RequestResponseMapper.LeaderAndSr, version);
        this.partitionStates = partitionStates;
    }

    public LeaderAndSrRequest(Struct struct, short version) {
        super(struct.getInt(ProtocolSchemaConstant.CONTROLLER_ID_KEY_NAME), struct.getInt(ProtocolSchemaConstant.CONTROLLER_EPOCH_KEY_NAME), RequestResponseMapper.LeaderAndSr, version);
        for (Object partitionStateObj : struct.getArray(ProtocolSchemaConstant.PARTITION_STATES_KEY_NAME)) {
            Struct partitionStateStruct = (Struct) partitionStateObj;
            int leaderId = partitionStateStruct.getInt(ProtocolSchemaConstant.LEADER_KEY_NAME);
            int leaderEpoch = partitionStateStruct.getInt(ProtocolSchemaConstant.LEADER_EPOCH);
            int controllerEpoch = partitionStateStruct.getInt(ProtocolSchemaConstant.CONTROLLER_EPOCH_KEY_NAME);

            Object[] srArray = partitionStateStruct.getArray(ProtocolSchemaConstant.SR_KEY_NAME);
            List<Integer> srReplicas = new ArrayList<>();
            for (Object sr : srArray) {
                srReplicas.add((Integer) sr);
            }

            Object[] replicasArray = partitionStateStruct.getArray(ProtocolSchemaConstant.REPLICAS_KEY_NAME);
            List<Integer> replicas = new ArrayList<>();
            for (Object replica : replicasArray) {
                replicas.add((Integer) replica);
            }

            Object[] offlineRsArray = partitionStateStruct.getArray(ProtocolSchemaConstant.OFFLINE_REPLICAS_KEY_NAME);
            List<Integer> offlineReplicas = new ArrayList<>();
            for (Object offlineReplica : offlineRsArray) {
                offlineReplicas.add((Integer) offlineReplica);
            }

            PartitionState partitionState = new PartitionState();
            partitionState.setControllerEpoch(controllerEpoch);
            partitionState.setLeaderEpoch(leaderEpoch);
            partitionState.setLeaderId(leaderId);
            partitionState.setReplicas(replicas);
            partitionState.setSrReplicas(srReplicas);
            partitionState.setOfflineReplicas(offlineReplicas);

            String topicName = partitionStateStruct.getString(ProtocolSchemaConstant.TOPIC_KEY_NAME);
            int partition = partitionStateStruct.getInt(ProtocolSchemaConstant.PARTITION_KEY_NAME);
            TopicPartition tp = new TopicPartition(topicName, partition);
            if (partitionStates == null) {
                partitionStates = new HashMap<>();
            }
            partitionStates.put(tp, partitionState);
        }

    }

    public Map<TopicPartition, PartitionState> getPartitionStates() {
        return partitionStates;
    }

    public void setPartitionStates(Map<TopicPartition, PartitionState> partitionStates) {
        this.partitionStates = partitionStates;
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.LeaderAndSr.getRequestSchema(getVersion()));
        List<Struct> partitionStateStructs = new ArrayList<>();
        for (Map.Entry<TopicPartition, PartitionState> entry : partitionStates.entrySet()) {
            Struct partitionStateStruct = new Struct(LEADER_AND_SR_REQUEST_PARTITION_STATE_V0);
            partitionStateStruct.set(ProtocolSchemaConstant.TOPIC_KEY_NAME, entry.getKey().getTopic());
            partitionStateStruct.set(ProtocolSchemaConstant.PARTITION_KEY_NAME, entry.getKey().getPartition());
            partitionStateStruct.set(ProtocolSchemaConstant.CONTROLLER_EPOCH_KEY_NAME, entry.getValue().getControllerEpoch());
            partitionStateStruct.set(ProtocolSchemaConstant.LEADER_KEY_NAME, entry.getValue().getLeaderId());
            partitionStateStruct.set(ProtocolSchemaConstant.LEADER_EPOCH, entry.getValue().getLeaderEpoch());
            partitionStateStruct.set(ProtocolSchemaConstant.SR_KEY_NAME, entry.getValue().getSrReplicas().toArray());
            partitionStateStruct.set(ProtocolSchemaConstant.REPLICAS_KEY_NAME, entry.getValue().getReplicas().toArray());
            partitionStateStruct.set(ProtocolSchemaConstant.IS_NEW_KEY_NAME, entry.getValue().isNew());
            partitionStateStruct.set(ProtocolSchemaConstant.OFFLINE_REPLICAS_KEY_NAME, new Object[]{});
            partitionStateStructs.add(partitionStateStruct);
        }
        struct.set(ProtocolSchemaConstant.CONTROLLER_ID_KEY_NAME, controllerId());
        struct.set(ProtocolSchemaConstant.CONTROLLER_EPOCH_KEY_NAME, controllerEpoch());
        struct.set(ProtocolSchemaConstant.PARTITION_STATES_KEY_NAME, partitionStateStructs.toArray());
        return struct;
    }

    @Override
    public LeaderAndSrResponse getErrorResponse(int throttleTime, Throwable e) {
        ErrorCodes errorCodes = ErrorCodes.forException(e);
        Map<TopicPartition, ErrorCodes> responses = new HashMap<>(partitionStates.size());
        for (TopicPartition partition : partitionStates.keySet()) {
            responses.put(partition, errorCodes);
        }
        short versionId = getVersion();
        switch (versionId) {
            case 0:
            case 1:
                return new LeaderAndSrResponse((short) 0, errorCodes, responses);
            default:
                throw new IllegalArgumentException(String.format("Version %d is not valid. Valid versions for %s are 0 to %d", versionId, this.getClass().getSimpleName(), RequestResponseMapper.LeaderAndSr.maxVersion()));
        }
    }

    public static Schema[] schemaVersions() {
        return new Schema[]{LEADER_AND_SR_REQUEST_V0};
    }

    public static LeaderAndSrRequest parse(ByteBuffer buffer, short version) {
        return new LeaderAndSrRequest(RequestResponseMapper.LeaderAndSr.parseRequest(version, buffer), version);
    }

}
 