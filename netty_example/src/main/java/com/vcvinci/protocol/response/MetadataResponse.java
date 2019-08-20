package com.vcvinci.protocol.response;

import com.vcvinci.common.cluster.Broker;
import com.vcvinci.common.cluster.Cluster;
import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.exception.api.InvalidMetadataException;
import com.vcvinci.common.metadata.MetadataVersion;
import com.vcvinci.common.partition.PartitionInfo;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.CommonTypes.ARRAY;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.util.RequestResponseMapper;
import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * metadata响应格式
 *
 * @author vinci
 * @version 创建时间：2018年5月23日 上午11:35:18
 */
public class MetadataResponse extends AbstractResponse {

    private static final Schema METADATA_BROKER_V0 = new Schema(CommonField.BROKER_ID, CommonField.HOST,
            CommonField.PORT);

    private static final Schema PARTITION_METADATA_V0 = new Schema(
            CommonField.ERROR_CODE,
            CommonField.PARTITION_ID,
            new Field(ProtocolSchemaConstant.LEADER_KEY_NAME, CommonTypes.INT32, "The id of the broker acting as leader for this partition."),
            new Field(ProtocolSchemaConstant.REPLICAS_KEY_NAME, new ARRAY<>(CommonTypes.INT32), "The set of all nodes that host this partition."),
            new Field(ProtocolSchemaConstant.SR_KEY_NAME, new ARRAY<>(CommonTypes.INT32), "The set of nodes that are in sync with the leader for this partition."),
            new Field(ProtocolSchemaConstant.OFFLINE_REPLICAS_KEY_NAME, new ARRAY<>(CommonTypes.INT32), "The set of offline replicas of this partition."));

    private static final Schema TOPIC_METADATA_V0 = new Schema(
            CommonField.ERROR_CODE,
            CommonField.TOPIC_NAME,
            new Field(ProtocolSchemaConstant.IS_INTERNAL_KEY_NAME, CommonTypes.BOOLEAN, "Indicates if the topic is considered a dove internal topic"),
            new Field(ProtocolSchemaConstant.PARTITION_METADATA_KEY_NAME, new ARRAY<>(PARTITION_METADATA_V0), "Metadata for each partition of the topic."));

    private static final Schema METADATA_RESPONSE_V0 = new Schema(
            /*作用：当前元数据的版本, 每次心跳时客户端会带上这版本号, 和元数据缓存比较，如果不一致就会在心跳响应时，带上 MetadataResponse 数据*/
            new Field(ProtocolSchemaConstant.METADATA_VERSION_KEY_NAME, CommonTypes.INT32),
            new Field(ProtocolSchemaConstant.CONTROLLER_EPOCH_KEY_NAME, CommonTypes.INT32),
            new Field(ProtocolSchemaConstant.BROKERS_KEY_NAME, new ARRAY<>(METADATA_BROKER_V0), "Host and port information for all brokers."),
            new Field(ProtocolSchemaConstant.CLUSTER_ID_KEY_NAME, CommonTypes.STRING, "The cluster id that this broker belongs to."),
            new Field(ProtocolSchemaConstant.CONTROLLER_ID_KEY_NAME, CommonTypes.INT32, "The broker id of the controller broker."),
            new Field(ProtocolSchemaConstant.TOPIC_METADATA_KEY_NAME, new ARRAY<>(TOPIC_METADATA_V0)));

    public static Schema[] schemaVersions() {
        return new Schema[]{METADATA_RESPONSE_V0};
    }

    private final Collection<Broker> brokers;
    private final Broker controller;
    private final List<TopicMetadata> topicMetadata;
    private final String clusterId;
    private final MetadataVersion metadataVersion;

    public MetadataResponse(short version, List<Broker> brokers, String clusterId, int controllerId, List<TopicMetadata> topicMetadata, MetadataVersion metadataVersion) {
        super(version);
        this.brokers = brokers;
        this.controller = getControllerBroker(controllerId, brokers);
        this.topicMetadata = topicMetadata;
        this.clusterId = clusterId;
        this.metadataVersion = metadataVersion;
    }

    public MetadataResponse(Struct struct, short version) {
        super(version);
        this.metadataVersion = new MetadataVersion(struct.getInt(ProtocolSchemaConstant.CONTROLLER_EPOCH_KEY_NAME), struct.getInt(ProtocolSchemaConstant.METADATA_VERSION_KEY_NAME));
        Map<Integer, Broker> brokers = new HashMap<>();
        Object[] brokerStructs = (Object[]) struct.get(ProtocolSchemaConstant.BROKERS_KEY_NAME);
        for (Object brokerStruct : brokerStructs) {
            Struct broker = (Struct) brokerStruct;
            int nodeId = broker.get(CommonField.BROKER_ID);
            String host = broker.get(CommonField.HOST);
            int port = broker.get(CommonField.PORT);
            brokers.put(nodeId, new Broker(nodeId, host, port));
        }

        // This field only exists in v1+
        // When we can't know the controller id in a v0 response we default to NO_CONTROLLER_ID
        int controllerId = ProtocolSchemaConstant.NO_CONTROLLER_ID;
        if (struct.hasField(ProtocolSchemaConstant.CONTROLLER_ID_KEY_NAME)) {
            controllerId = struct.getInt(ProtocolSchemaConstant.CONTROLLER_ID_KEY_NAME);
        }

        // This field only exists in v2+
        if (struct.hasField(ProtocolSchemaConstant.CLUSTER_ID_KEY_NAME)) {
            this.clusterId = struct.getString(ProtocolSchemaConstant.CLUSTER_ID_KEY_NAME);
        } else {
            this.clusterId = null;
        }

        List<TopicMetadata> topicMetadata = new ArrayList<>();
        Object[] topicInfos = (Object[]) struct.get(ProtocolSchemaConstant.TOPIC_METADATA_KEY_NAME);
        for (Object topicInfoObj : topicInfos) {
            Struct topicInfo = (Struct) topicInfoObj;
            ErrorCodes topicError = ErrorCodes.forCode(topicInfo.get(CommonField.ERROR_CODE));
            String topic = topicInfo.get(CommonField.TOPIC_NAME);
            // This field only exists in v1+
            // When we can't know if a topic is internal or not in a v0 response we default to false
            boolean isInternal = topicInfo.hasField(ProtocolSchemaConstant.IS_INTERNAL_KEY_NAME) ? topicInfo.getBoolean(ProtocolSchemaConstant.IS_INTERNAL_KEY_NAME) : false;

            List<PartitionMetadata> partitionMetadata = new ArrayList<>();

            Object[] partitionInfos = (Object[]) topicInfo.get(ProtocolSchemaConstant.PARTITION_METADATA_KEY_NAME);
            for (Object partitionInfoObj : partitionInfos) {
                Struct partitionInfo = (Struct) partitionInfoObj;
                ErrorCodes partitionError = ErrorCodes.forCode(partitionInfo.get(CommonField.ERROR_CODE));
                int partition = partitionInfo.get(CommonField.PARTITION_ID);
                int leader = partitionInfo.getInt(ProtocolSchemaConstant.LEADER_KEY_NAME);
                Broker leaderBroker = leader == -1 ? null : brokers.get(leader);

                Object[] replicas = (Object[]) partitionInfo.get(ProtocolSchemaConstant.REPLICAS_KEY_NAME);
                List<Broker> replicaBrokers = convertToBrokers(brokers, replicas);

                Object[] isr = (Object[]) partitionInfo.get(ProtocolSchemaConstant.SR_KEY_NAME);
                List<Broker> isrBrokers = convertToBrokers(brokers, isr);

                Object[] offlineReplicas = partitionInfo.hasField(ProtocolSchemaConstant.OFFLINE_REPLICAS_KEY_NAME) ?
                        (Object[]) partitionInfo.get(ProtocolSchemaConstant.OFFLINE_REPLICAS_KEY_NAME) : new Object[0];
                List<Broker> offlineBrokers = convertToBrokers(brokers, offlineReplicas);

                partitionMetadata.add(new PartitionMetadata(partitionError, partition, leaderBroker, replicaBrokers, isrBrokers, offlineBrokers));
            }

            topicMetadata.add(new TopicMetadata(topicError, topic, isInternal, partitionMetadata));
        }

        this.brokers = brokers.values();
        this.controller = getControllerBroker(controllerId, brokers.values());
        this.topicMetadata = topicMetadata;
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.Metadata.getResponseSchema(getVersion()));
        struct.set(ProtocolSchemaConstant.CONTROLLER_EPOCH_KEY_NAME, metadataVersion.getControllerEpoch());
        struct.set(ProtocolSchemaConstant.METADATA_VERSION_KEY_NAME, metadataVersion.getMetadataVersion());
        //struct.setIfExists(THROTTLE_TIME_MS, throttleTimeMs);
        List<Struct> brokerArray = new ArrayList<>();
        for (Broker broker : brokers) {
            Struct brokerStruct = struct.instance(ProtocolSchemaConstant.BROKERS_KEY_NAME);
            brokerStruct.set(CommonField.BROKER_ID, broker.getId());
            brokerStruct.set(CommonField.HOST, broker.getHost());
            brokerStruct.set(CommonField.PORT, broker.getPort());
            brokerArray.add(brokerStruct);
        }
        struct.set(ProtocolSchemaConstant.BROKERS_KEY_NAME, brokerArray.toArray());

        if (struct.hasField(ProtocolSchemaConstant.CONTROLLER_ID_KEY_NAME)) {
            struct.set(ProtocolSchemaConstant.CONTROLLER_ID_KEY_NAME, controller == null ? ProtocolSchemaConstant.NO_CONTROLLER_ID : controller.getId());
        }

        if (struct.hasField(ProtocolSchemaConstant.CLUSTER_ID_KEY_NAME)) {
            struct.set(ProtocolSchemaConstant.CLUSTER_ID_KEY_NAME, clusterId);
        }

        List<Struct> topicMetadataArray = new ArrayList<>(topicMetadata.size());
        for (TopicMetadata metadata : topicMetadata) {
            Struct topicData = struct.instance(ProtocolSchemaConstant.TOPIC_METADATA_KEY_NAME);
            topicData.set(CommonField.TOPIC_NAME, metadata.topic);
            topicData.set(CommonField.ERROR_CODE, metadata.error.code());
            if (topicData.hasField(ProtocolSchemaConstant.IS_INTERNAL_KEY_NAME)) {
                topicData.set(ProtocolSchemaConstant.IS_INTERNAL_KEY_NAME, metadata.isInternal());
            }

            List<Struct> partitionMetadataArray = new ArrayList<>(metadata.partitionMetadata.size());
            for (PartitionMetadata partitionMetadata : metadata.partitionMetadata()) {
                Struct partitionData = topicData.instance(ProtocolSchemaConstant.PARTITION_METADATA_KEY_NAME);
                partitionData.set(CommonField.ERROR_CODE, partitionMetadata.error.code());
                partitionData.set(CommonField.PARTITION_ID, partitionMetadata.partition);
                partitionData.set(ProtocolSchemaConstant.LEADER_KEY_NAME, partitionMetadata.leaderId());
                ArrayList<Integer> replicas = new ArrayList<>(partitionMetadata.replicas.size());
                for (Broker broker : partitionMetadata.replicas) {
                    replicas.add(broker.getId());
                }
                partitionData.set(ProtocolSchemaConstant.REPLICAS_KEY_NAME, replicas.toArray());
                ArrayList<Integer> isr = new ArrayList<>(partitionMetadata.isr.size());
                for (Broker broker : partitionMetadata.isr) {
                    isr.add(broker.getId());
                }
                partitionData.set(ProtocolSchemaConstant.SR_KEY_NAME, isr.toArray());
                if (partitionData.hasField(ProtocolSchemaConstant.OFFLINE_REPLICAS_KEY_NAME)) {
                    ArrayList<Integer> offlineReplicas = new ArrayList<>(partitionMetadata.offlineReplicas.size());
                    for (Broker broker : partitionMetadata.offlineReplicas) {
                        offlineReplicas.add(broker.getId());
                    }
                    partitionData.set(ProtocolSchemaConstant.OFFLINE_REPLICAS_KEY_NAME, offlineReplicas.toArray());
                }
                partitionMetadataArray.add(partitionData);
            }
            topicData.set(ProtocolSchemaConstant.PARTITION_METADATA_KEY_NAME, partitionMetadataArray.toArray());
            topicMetadataArray.add(topicData);
        }
        struct.set(ProtocolSchemaConstant.TOPIC_METADATA_KEY_NAME, topicMetadataArray.toArray());
        return struct;
    }

    /**
     * 功能描述: 获取有元数据错误的主题的映射。
     *
     * @author vinci
     * @date 2018/6/25 14:52
     * @return {@code java.util.Map<java.lang.String, com.vcvinci.common.exception.Error> }
     */
    public Map<String, ErrorCodes> errors() {
        Map<String, ErrorCodes> errors = new HashMap<>();
        for (TopicMetadata metadata : topicMetadata) {
            if (metadata.error != ErrorCodes.NONE) {
                errors.put(metadata.topic(), metadata.error);
            }
        }
        return errors;
    }

    /**
     * 功能描述:返回一组主题，其中有一个错误指示无效的元数据和任何错误表明元数据无效的分区的主题。
     * 这包括元数据请求中指定的所有不存在的主题。
     * 以及返回一个或多个不知道leader的分区的任何主题。
     *
     * @return java.util.Set<java.lang.String>
     * @author vinci
     * @date 2018/6/25 14:53
     */
    public Set<String> unavailableTopics() {
        Set<String> invalidMetadataTopics = new HashSet<>();
        for (TopicMetadata topicMetadata : this.topicMetadata) {
            if (topicMetadata.error.instanceOf(InvalidMetadataException.class)) {
                invalidMetadataTopics.add(topicMetadata.topic);
            } else {
                for (PartitionMetadata partitionMetadata : topicMetadata.partitionMetadata) {
                    if (partitionMetadata.error.instanceOf(InvalidMetadataException.class)) {
                        invalidMetadataTopics.add(topicMetadata.topic);
                        break;
                    }
                }
            }
        }
        return invalidMetadataTopics;
    }

    /**
     * 功能描述: 从此响应获取集群元数据的快照
     *
     * @return com.vcvinci.common.cluster.Cluster
     * @author vinci
     * @date 2018/6/25 14:54
     */
    public Cluster cluster() {
        Set<String> internalTopics = new HashSet<String>();
        List<PartitionInfo> partitions = new ArrayList<PartitionInfo>();
        for (TopicMetadata metadata : topicMetadata) {
            if (metadata.error == ErrorCodes.NONE) {
                if (metadata.isInternal) {
                    internalTopics.add(metadata.topic);
                }
                for (PartitionMetadata partitionMetadata : metadata.partitionMetadata) {
                    partitions.add(new PartitionInfo(
                            metadata.topic,
                            partitionMetadata.partition,
                            partitionMetadata.leader,
                            partitionMetadata.replicas.toArray(new Broker[0]),
                            partitionMetadata.isr.toArray(new Broker[0]),
                            partitionMetadata.offlineReplicas.toArray(new Broker[0])));
                }
            }
        }

        return new Cluster(this.clusterId, this.brokers, partitions, topicsByError(ErrorCodes.API_TOPIC_AUTHORIZATION_FAILED_ERROR),
                internalTopics, this.controller);
    }

    /**
     *
     * 功能描述: 获取metadata的版本信息
     *
     * @return: metadata的版本信息
     * @auther: yuxin.chen01
     * @since: 2018/12/10 17:26
     */
    public MetadataVersion metadataVersion(){
        return this.metadataVersion;
    }

    /**
     * 功能描述:返回指定错误的主题集
     *
     * @param error 异常枚举
     * @return java.util.Set<java.lang.String>
     * @author vinci
     * @date 2018/6/25 14:55
     */
    public Set<String> topicsByError(ErrorCodes error) {
        Set<String> errorTopics = new HashSet<String>();
        for (TopicMetadata metadata : topicMetadata) {
            if (metadata.error == error) {
                errorTopics.add(metadata.topic());
            }
        }
        return errorTopics;
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 功能描述: 某个topic的分区信息
     *
     * @author vinci
     * @date 2018/11/26 下午2:22
     */
    public static class TopicMetadata {

        private final ErrorCodes error;
        private final String topic;
        private final boolean isInternal;
        private final List<PartitionMetadata> partitionMetadata;

        public TopicMetadata(ErrorCodes error,
                             String topic,
                             boolean isInternal,
                             List<PartitionMetadata> partitionMetadata) {
            this.error = error;
            this.topic = topic;
            this.isInternal = isInternal;
            this.partitionMetadata = partitionMetadata;
        }

        public ErrorCodes error() {
            return error;
        }

        public String topic() {
            return topic;
        }

        public boolean isInternal() {
            return isInternal;
        }

        public List<PartitionMetadata> partitionMetadata() {
            return partitionMetadata;
        }

    }

    /**
     * 功能描述: 这用于描述元数据的每个分区状态。
     *
     * @author vinci
     * @date 2018/11/26 下午2:22
     */
    public static class PartitionMetadata {

        private final ErrorCodes error;
        private final int partition;
        private final Broker leader;
        private final List<Broker> replicas;
        private final List<Broker> isr;
        private final List<Broker> offlineReplicas;

        public PartitionMetadata(ErrorCodes error,
                                 int partition,
                                 Broker leader,
                                 List<Broker> replicas,
                                 List<Broker> isr,
                                 List<Broker> offlineReplicas) {
            this.error = error;
            this.partition = partition;
            this.leader = leader;
            this.replicas = replicas;
            this.isr = isr;
            this.offlineReplicas = offlineReplicas;
        }

        public ErrorCodes error() {
            return error;
        }

        public int partition() {
            return partition;
        }

        public int leaderId() {
            return leader == null ? -1 : leader.getId();
        }

        public Broker leader() {
            return leader;
        }

        public List<Broker> replicas() {
            return replicas;
        }

        public List<Broker> isr() {
            return isr;
        }

        public List<Broker> offlineReplicas() {
            return offlineReplicas;
        }

        @Override
        public String toString() {
            return "(type=PartitionMetadata" +
                    ", error=" + error +
                    ", partition=" + partition +
                    ", leader=" + leader +
                    ", replicas=" + StringUtils.join(replicas, ",") +
                    ", isr=" + StringUtils.join(isr, ",") +
                    ", offlineReplicas=" + StringUtils.join(offlineReplicas, ",") + ')';
        }
    }

    private List<Broker> convertToBrokers(Map<Integer, Broker> brokers, Object[] brokerIds) {
        List<Broker> nodes = new ArrayList<>(brokerIds.length);
        for (Object brokerId : brokerIds) {
            if (brokers.containsKey(brokerId)) {
                nodes.add(brokers.get(brokerId));
            } else {
                nodes.add(new Broker((int) brokerId, "", -1));
            }
        }
        return nodes;
    }

    private Broker getControllerBroker(int controllerId, Collection<Broker> brokers) {
        for (Broker broker : brokers) {
            if (broker.getId() == controllerId) {
                return broker;
            }
        }
        return null;
    }

    public static MetadataResponse parse(ByteBuffer buffer, short version) {
        return new MetadataResponse(RequestResponseMapper.Metadata.parseResponse(version, buffer), version);
    }
}
