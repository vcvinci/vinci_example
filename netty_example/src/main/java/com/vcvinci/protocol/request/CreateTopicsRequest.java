/**
 * FileName: CreateTopicsRequest
 * Author:   HuangTaiHong
 * Date:     2018/12/1 9:42
 * Description: 创建Topic请求
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.vcvinci.protocol.request;

import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.response.Response;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.vcvinci.common.schema.CommonTypes.BOOLEAN;
import static com.vcvinci.common.schema.CommonTypes.INT16;
import static com.vcvinci.common.schema.CommonTypes.INT32;
import static com.vcvinci.common.schema.CommonTypes.NULLABLE_STRING;
import static com.vcvinci.common.schema.CommonTypes.STRING;

/**
 * 〈一句话功能简述〉<br>
 * 〈创建Topic请求〉
 *
 * @author HuangTaiHong
 * @create 2018/12/1
 * @since 1.0.0
 */
public class CreateTopicsRequest extends AbstractRequest {
    private static final String REQUESTS_KEY_NAME = "create_topic_requests";

    private static final Schema PARTITION_REPLICA_ASSIGNMENT_ENTRY = new Schema(
            new Field(ProtocolSchemaConstant.PARTITION_KEY_NAME, INT32, "Topic partition id"),
            new Field(ProtocolSchemaConstant.REPLICAS_KEY_NAME, new CommonTypes.ARRAY<>(INT32), "The set of all nodes that should host this partition. The first replica in the list is the preferred leader."));

    public static final Schema CONFIG_ENTRY = new Schema(
            new Field(ProtocolSchemaConstant.CONFIG_NAME_KEY_NAME, STRING, "Configuration name"),
            new Field(ProtocolSchemaConstant.CONFIG_VALUE_KEY_NAME, NULLABLE_STRING, "Configuration value"));

    public static final Schema CREATE_TOPIC_REQUEST = new Schema(
            new Field(ProtocolSchemaConstant.TOPIC_KEY_NAME, STRING, "Name of topic"),
            new Field(ProtocolSchemaConstant.NUM_PARTITIONS, INT32, "Number of partitions to be created. -1 indicates unset."),
            new Field(ProtocolSchemaConstant.REPLICATION_FACTOR_KEY_NAME, INT16, "Replication factor for the topic. -1 indicates unset."),
            new Field(ProtocolSchemaConstant.REPLICA_ASSIGNMENT_KEY_NAME, new CommonTypes.ARRAY<>(PARTITION_REPLICA_ASSIGNMENT_ENTRY), "Replica assignment among dove brokers for this topic partitions. If this is set num_partitions and replication_factor must be unset."),
            new Field(ProtocolSchemaConstant.CONFIG_ENTRIES_KEY_NAME, new CommonTypes.ARRAY<>(CONFIG_ENTRY), "Topic level configuration for topic to be set."));

    private static final Schema CREATE_TOPICS_REQUEST = new Schema(
            new Field(REQUESTS_KEY_NAME, new CommonTypes.ARRAY<>(CREATE_TOPIC_REQUEST), "An array of single topic creation requests. Can not have multiple entries for the same topic."),
            new Field(ProtocolSchemaConstant.TIMEOUT_KEY_NAME, INT32, "The time in ms to wait for a topic to be completely created on the controller node. Values <= 0 will trigger topic creation and return immediately"),
            new Field(ProtocolSchemaConstant.VALIDATE_ONLY_KEY_NAME, BOOLEAN, "If this is true, the request will be validated, but the topic won't be created."));

    public static Schema[] schemaVersions() {
        return new Schema[]{CREATE_TOPICS_REQUEST};
    }

    private final Integer timeout;
    private final boolean validateOnly;
    private final Map<String, TopicDetails> topics;
    // 一个请求中包含相同的TOPIC 用于返回错误码时使用
    private final Set<String> duplicateTopics;

    public CreateTopicsRequest(Map<String, TopicDetails> topics, Integer timeout, boolean validateOnly, short version) {
        super(RequestResponseMapper.CreateTopics, version);
        this.topics = topics;
        this.timeout = timeout;
        this.validateOnly = validateOnly;
        this.duplicateTopics = Collections.emptySet();
    }

    public CreateTopicsRequest(Struct struct, short version) {
        super(RequestResponseMapper.CreateTopics, version);
        Object[] requestStructs = struct.getArray(REQUESTS_KEY_NAME);
        Map<String, TopicDetails> topics = new HashMap<>();
        Set<String> duplicateTopics = new HashSet<>();
        for (Object requestStructObj : requestStructs) {
            Struct singleRequestStruct = (Struct) requestStructObj;
            String topic = singleRequestStruct.getString(ProtocolSchemaConstant.TOPIC_KEY_NAME);
            if (topics.containsKey(topic)) {
                duplicateTopics.add(topic);
            }
            int numPartitions = singleRequestStruct.getInt(ProtocolSchemaConstant.NUM_PARTITIONS);
            short replicationFactor = singleRequestStruct.getShort(ProtocolSchemaConstant.REPLICATION_FACTOR_KEY_NAME);
            // 指定副本分配
            Object[] assignmentsArray = singleRequestStruct.getArray(ProtocolSchemaConstant.REPLICA_ASSIGNMENT_KEY_NAME);
            Map<Integer, List<Integer>> partitionReplicaAssignments = new HashMap<>(assignmentsArray.length);
            for (Object assignmentStructObj : assignmentsArray) {
                Struct assignmentStruct = (Struct) assignmentStructObj;
                Integer partitionId = assignmentStruct.getInt(ProtocolSchemaConstant.PARTITION_KEY_NAME);
                Object[] replicasArray = assignmentStruct.getArray(ProtocolSchemaConstant.REPLICAS_KEY_NAME);
                List<Integer> replicas = new ArrayList<>(replicasArray.length);
                for (Object replica : replicasArray) {
                    replicas.add((Integer) replica);
                }
                partitionReplicaAssignments.put(partitionId, replicas);
            }

            // 指定Topic配置
            Object[] configArray = singleRequestStruct.getArray(ProtocolSchemaConstant.CONFIG_ENTRIES_KEY_NAME);
            Map<String, String> configs = new HashMap<>(configArray.length);
            for (Object configStructObj : configArray) {
                Struct configStruct = (Struct) configStructObj;
                String key = configStruct.getString(ProtocolSchemaConstant.CONFIG_NAME_KEY_NAME);
                String value = configStruct.getString(ProtocolSchemaConstant.CONFIG_VALUE_KEY_NAME);
                configs.put(key, value);
            }
            TopicDetails args = new TopicDetails(numPartitions, replicationFactor, partitionReplicaAssignments, configs);
            topics.put(topic, args);
        }

        this.topics = topics;
        this.timeout = struct.getInt(ProtocolSchemaConstant.TIMEOUT_KEY_NAME);
        if (struct.hasField(ProtocolSchemaConstant.VALIDATE_ONLY_KEY_NAME)) {
            this.validateOnly = struct.getBoolean(ProtocolSchemaConstant.VALIDATE_ONLY_KEY_NAME);
        } else {
            this.validateOnly = false;
        }
        this.duplicateTopics = duplicateTopics;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public boolean isValidateOnly() {
        return validateOnly;
    }

    public Map<String, TopicDetails> getTopics() {
        return topics;
    }

    public Set<String> getDuplicateTopics() {
        return duplicateTopics;
    }

    @Override
    public Response getErrorResponse(int throttleTime, Throwable e) {
        return null;
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.CreateTopics.getRequestSchema(getVersion()));
        List<Struct> createTopicRequestStructs = new ArrayList<>(topics.size());
        for (Map.Entry<String, TopicDetails> entry : topics.entrySet()) {
            Struct singleRequestStruct = struct.instance(REQUESTS_KEY_NAME);
            String topic = entry.getKey();
            TopicDetails args = entry.getValue();
            singleRequestStruct.set(ProtocolSchemaConstant.TOPIC_KEY_NAME, topic);
            singleRequestStruct.set(ProtocolSchemaConstant.NUM_PARTITIONS, args.numPartitions);
            singleRequestStruct.set(ProtocolSchemaConstant.REPLICATION_FACTOR_KEY_NAME, args.replicationFactor);

            // 指定副本分配
            List<Struct> replicaAssignmentsStructs = new ArrayList<>(args.replicasAssignments.size());
            for (Map.Entry<Integer, List<Integer>> partitionReplicaAssignment : args.replicasAssignments.entrySet()) {
                Struct replicaAssignmentStruct = singleRequestStruct.instance(ProtocolSchemaConstant.REPLICA_ASSIGNMENT_KEY_NAME);
                replicaAssignmentStruct.set(ProtocolSchemaConstant.PARTITION_KEY_NAME, partitionReplicaAssignment.getKey());
                replicaAssignmentStruct.set(ProtocolSchemaConstant.REPLICAS_KEY_NAME, partitionReplicaAssignment.getValue().toArray());
                replicaAssignmentsStructs.add(replicaAssignmentStruct);
            }
            singleRequestStruct.set(ProtocolSchemaConstant.REPLICA_ASSIGNMENT_KEY_NAME, replicaAssignmentsStructs.toArray());

            // 指定Topic配置
            List<Struct> configsStructs = new ArrayList<>(args.configs.size());
            for (Map.Entry<String, String> configEntry : args.configs.entrySet()) {
                Struct configStruct = singleRequestStruct.instance(ProtocolSchemaConstant.CONFIG_ENTRIES_KEY_NAME);
                configStruct.set(ProtocolSchemaConstant.CONFIG_NAME_KEY_NAME, configEntry.getKey());
                configStruct.set(ProtocolSchemaConstant.CONFIG_VALUE_KEY_NAME, configEntry.getValue());
                configsStructs.add(configStruct);
            }
            singleRequestStruct.set(ProtocolSchemaConstant.CONFIG_ENTRIES_KEY_NAME, configsStructs.toArray());
            createTopicRequestStructs.add(singleRequestStruct);
        }
        struct.set(REQUESTS_KEY_NAME, createTopicRequestStructs.toArray());
        struct.set(ProtocolSchemaConstant.TIMEOUT_KEY_NAME, timeout);
        struct.set(ProtocolSchemaConstant.VALIDATE_ONLY_KEY_NAME, validateOnly);
        return struct;
    }

    public static final int NO_NUM_PARTITIONS = -1;
    public static final short NO_REPLICATION_FACTOR = -1;

    public static final class TopicDetails {
        public final int numPartitions;
        public final short replicationFactor;
        public final Map<Integer, List<Integer>> replicasAssignments;
        public final Map<String, String> configs;

        private TopicDetails(int numPartitions, short replicationFactor, Map<Integer, List<Integer>> replicasAssignments, Map<String, String> configs) {
            this.numPartitions = numPartitions;
            this.replicationFactor = replicationFactor;
            this.replicasAssignments = replicasAssignments;
            this.configs = configs;
        }

        public TopicDetails(int partitions, short replicationFactor, Map<String, String> configs) {
            this(partitions, replicationFactor, Collections.<Integer, List<Integer>>emptyMap(), configs);
        }

        public TopicDetails(int partitions, short replicationFactor) {
            this(partitions, replicationFactor, Collections.<String, String>emptyMap());
        }

        public TopicDetails(Map<Integer, List<Integer>> replicasAssignments, Map<String, String> configs) {
            this(NO_NUM_PARTITIONS, NO_REPLICATION_FACTOR, replicasAssignments, configs);
        }

        public TopicDetails(Map<Integer, List<Integer>> replicasAssignments) {
            this(replicasAssignments, Collections.<String, String>emptyMap());
        }


        public int getNumPartitions() {
            return numPartitions;
        }

        public short getReplicationFactor() {
            return replicationFactor;
        }

        public Map<Integer, List<Integer>> getReplicasAssignments() {
            return replicasAssignments;
        }

        public Map<String, String> getConfigs() {
            return configs;
        }

        @Override
        public String toString() {
            StringBuilder bld = new StringBuilder();
            bld.append("(numPartitions=").append(numPartitions).
                    append(", replicationFactor=").append(replicationFactor).
                    append(", replicasAssignments=").append(replicasAssignments).
                    append(", configs=").append(configs).
                    append(")");
            return bld.toString();
        }
    }
}