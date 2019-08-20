package com.vcvinci.protocol.request;

import com.vcvinci.common.cluster.Broker;
import com.vcvinci.common.partition.PartitionState;
import com.vcvinci.common.partition.TopicPartition;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.CommonTypes.ARRAY;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.response.AbstractResponse;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 上午7:32:36
 * @description 类说明
 */
public class UpdateMetadataRequest extends ControllerRequest {

    private static final Schema BROKER_V0 = new Schema(CommonField.BROKER_ID, CommonField.HOST, CommonField.PORT);

    private static final Schema PARTITION_STATES_V0 = new Schema(CommonField.TOPIC_NAME, CommonField.PARTITION_ID,
            CommonField.CONTROLLER_EPOCH, CommonField.LEADER_ID, CommonField.LEADER_EPOCH,
            new Field(ProtocolSchemaConstant.SR_KEY_NAME, new ARRAY<Integer>(CommonTypes.INT32),
                    "ids of the broker which in the sr"),
            new Field(ProtocolSchemaConstant.REPLICAS_KEY_NAME, new ARRAY<Integer>(CommonTypes.INT32),
                    "ids of broker which has the replica"),
            new Field(ProtocolSchemaConstant.OFFLINE_REPLICAS_KEY_NAME, new ARRAY<Integer>(CommonTypes.INT32),
                    "ids of the broker on which replica of this partition is offline"));

    private static final Schema UPDATE_METADATA_REQUEST_V0 = new Schema(CommonField.CONTROLLER_ID,
            CommonField.CONTROLLER_EPOCH,
            CommonField.METADATA_VERSION,
            new Field(ProtocolSchemaConstant.PARTITION_STATES_KEY_NAME, new ARRAY<Struct>(PARTITION_STATES_V0)),
            new Field(ProtocolSchemaConstant.LIVE_BROKERS_KEY_NAME, new ARRAY<Struct>(BROKER_V0)));

    private Map<TopicPartition, PartitionState> partitionStates;
    private Set<Broker> livingBrokers;
    private int metadataVersion;

    public UpdateMetadataRequest(Map<TopicPartition, PartitionState> partitionStates, Set<Broker> livingBrokers,
                                 int controllerId, int controllerEpoch, int metadataVersion, short version) {
        super(controllerId, controllerEpoch, RequestResponseMapper.UpdateMetadata, version);
        this.partitionStates = partitionStates;
        this.livingBrokers = livingBrokers;
        this.metadataVersion = metadataVersion;
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.UpdateMetadata.getRequestSchema(getVersion()));

        List<Struct> partitionStates = new ArrayList<>();
        for (Map.Entry<TopicPartition, PartitionState> entry : this.partitionStates.entrySet()) {

            Struct partitionStruct = new Struct(PARTITION_STATES_V0);

            TopicPartition tp = entry.getKey();
            partitionStruct.set(ProtocolSchemaConstant.TOPIC_KEY_NAME, tp.getTopic());
            partitionStruct.set(ProtocolSchemaConstant.PARTITION_KEY_NAME, tp.getPartition());

            PartitionState partitionState = entry.getValue();
            partitionStruct.set(ProtocolSchemaConstant.CONTROLLER_EPOCH_KEY_NAME, partitionState.getControllerEpoch());
            partitionStruct.set(ProtocolSchemaConstant.LEADER_KEY_NAME, partitionState.getLeaderId());
            partitionStruct.set(ProtocolSchemaConstant.LEADER_EPOCH, partitionState.getControllerEpoch());
            partitionStruct.set(ProtocolSchemaConstant.SR_KEY_NAME, partitionState.getSrReplicas().toArray());
            partitionStruct.set(ProtocolSchemaConstant.REPLICAS_KEY_NAME, partitionState.getReplicas().toArray());
            partitionStruct.set(ProtocolSchemaConstant.OFFLINE_REPLICAS_KEY_NAME,
                    partitionState.getOfflineReplicas().toArray());

            partitionStates.add(partitionStruct);
        }

        List<Struct> livingBrokers = new ArrayList<>();
        for (Broker broker : this.livingBrokers) {
            Struct brokerStruct = new Struct(BROKER_V0);
            brokerStruct.set(ProtocolSchemaConstant.BROKER_ID_KEY_NAME, broker.getId());
            brokerStruct.set(ProtocolSchemaConstant.HOST_KEY_NAME, broker.getHost());
            brokerStruct.set(ProtocolSchemaConstant.PORT_KEY_NAME, broker.getPort());
            livingBrokers.add(brokerStruct);
        }
        struct.set(ProtocolSchemaConstant.CONTROLLER_ID_KEY_NAME, controllerId());
        struct.set(ProtocolSchemaConstant.METADATA_VERSION_KEY_NAME, metadataVersion);
        struct.set(ProtocolSchemaConstant.PARTITION_STATES_KEY_NAME, partitionStates.toArray());
        struct.set(ProtocolSchemaConstant.LIVE_BROKERS_KEY_NAME, livingBrokers.toArray());
        struct.set(ProtocolSchemaConstant.CONTROLLER_EPOCH_KEY_NAME, controllerEpoch());
        return struct;
    }

    @Override
    public AbstractResponse getErrorResponse(int throttleTime, Throwable e) {
        // TODO Auto-generated method stub
        return null;
    }

    public static Schema[] schemaVersions() {
        return new Schema[]{UPDATE_METADATA_REQUEST_V0};
    }

    public UpdateMetadataRequest(Struct struct, short version) {
        super(struct.getInt(ProtocolSchemaConstant.CONTROLLER_ID_KEY_NAME),
                struct.getInt(ProtocolSchemaConstant.CONTROLLER_EPOCH_KEY_NAME), RequestResponseMapper.UpdateMetadata,
                version);
        this.metadataVersion = struct.getInt(ProtocolSchemaConstant.METADATA_VERSION_KEY_NAME);
        Map<TopicPartition, PartitionState> partitionStates = new HashMap<>();
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

            partitionStates.put(tp, partitionState);
        }

        Set<Broker> livingBrokers = new HashSet<>();
        for (Object brokerObject : struct.getArray(ProtocolSchemaConstant.LIVE_BROKERS_KEY_NAME)) {
            Struct brokerStruct = (Struct) brokerObject;

            int brokerId = brokerStruct.getInt(ProtocolSchemaConstant.BROKER_ID_KEY_NAME);
            String host = brokerStruct.getString(ProtocolSchemaConstant.HOST_KEY_NAME);
            int port = brokerStruct.getInt(ProtocolSchemaConstant.PORT_KEY_NAME);

            Broker broker = new Broker(brokerId, host, port);
            livingBrokers.add(broker);
        }

        this.livingBrokers = livingBrokers;
        this.partitionStates = partitionStates;

    }

    public Map<TopicPartition, PartitionState> partitionStates() {
        return this.partitionStates;
    }

    public Set<Broker> livingBrokers() {
        return this.livingBrokers;
    }

    public static UpdateMetadataRequest parse(ByteBuffer buffer, short version) {
        return new UpdateMetadataRequest(RequestResponseMapper.UpdateMetadata.parseRequest(version, buffer), version);
    }

    public int metadataVersion() {
        return metadataVersion;
    }
}
