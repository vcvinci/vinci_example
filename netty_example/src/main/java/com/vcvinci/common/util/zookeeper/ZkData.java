package com.vcvinci.common.util.zookeeper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vcvinci.common.cluster.Broker;
import com.vcvinci.common.cluster.BrokerInfo;
import com.vcvinci.common.partition.TopicPartition;
import com.vcvinci.common.sr.LeaderAndSr;
import com.vcvinci.common.sr.LeaderSrAndControllerEpoch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ZkData {
    private static String ROOT = "/dove";
    private static String Separator = "/";
    private static String ControllerPath = ROOT + "/controller";
    private static String ControllerEpochPath = ROOT + "/controller_epoch";
    private static String BrokersPath = ROOT + "/brokers";
    private static String BrokerIdsPath = BrokersPath + "/ids";
    private static String TopicsPath = ROOT + "/brokers/topics";
    private static String AdminPath = ROOT + "/admin";
    private static String DeleteTopicsPath = AdminPath + "/delete_topics";
    private static String ReassignPartitionsPath = AdminPath + "/reassign_partitions";
    private static String PreferredReplicaElection = AdminPath + "/preferred_replica_election";
    private static String BrokerShutdownPath = AdminPath + "/broker_shutdown";
    private static String SrChangeNotificationPath = ROOT + "/isr_change_notification";
    private static String CheckPointPath = ROOT + "/checkpoint";
    private static String ConsumerGroupPath = ROOT + "/consumer-group";
    private static String ConfigPath = ROOT + "/config";
    private static String ConfigEntityChangeNotificationPath = ConfigPath + "/changes";

    private static String VersionKey = "version";
    private static String TopicKey = "topic";
    private static String PartitionsKey = "partitions";
    private static String PartitionKey = "partition";
    private static String ControllerEpochKey = "controller_epoch";
    private static String LeaderKey = "leader";
    private static String LeaderEpochKey = "leader_epoch";
    private static String ReplicaKey = "replicas";
    private static String SrKey = "isr";
    private static String HostKey = "host";
    private static String PortKey = "port";
    private static String JmxPortKey = "jmx_port";
    private static String TimestampKey = "timestamp";

    private static String PartitionsUrl = "partitions";

    public static List<String> PersistentZkPaths = new ArrayList<>();

    static {
        PersistentZkPaths.add(ROOT);
        PersistentZkPaths.add(BrokersPath);
        PersistentZkPaths.add(BrokerIdsPath);
        PersistentZkPaths.add(TopicsPath);
        PersistentZkPaths.add(AdminPath);
        PersistentZkPaths.add(DeleteTopicsPath);
        PersistentZkPaths.add(PreferredReplicaElection);
        PersistentZkPaths.add(ReassignPartitionsPath);
        PersistentZkPaths.add(ConfigPath);
        PersistentZkPaths.add(ConfigEntityChangeNotificationPath);
        PersistentZkPaths.add(SrChangeNotificationPath);
        PersistentZkPaths.add(BrokerShutdownPath);
    }

    public static class ControllerZNode {

        public static String getPath() {
            return ControllerPath;
        }

        public static byte[] encode(int brokerId, long timestamp) {
            Map controllerNode = new HashMap();
            controllerNode.put("version", 1);
            controllerNode.put("brokerid", brokerId);
            controllerNode.put("timestamp", timestamp);
            return JSON.toJSONString(controllerNode).getBytes();
        }

        public static int decode(byte[] data) {
            JSONObject controllerNode = JSON.parseObject(new String(data));
            return controllerNode.getInteger("brokerid");
        }
    }

    public static class ControllerEpochZNode {

        public static String getPath() {
            return ControllerEpochPath;
        }

        public static byte[] encode(int epoch) {
            return String.valueOf(epoch).getBytes();
        }

        public static int decode(byte[] data) {
            return Integer.parseInt(new String(data));
        }
    }

    public static class BrokersZNode {

        public static String getPath() {
            return BrokersPath;
        }
    }

    public static class BrokerIdsZNode {

        public static String getPath() {
            return BrokerIdsPath;
        }
    }

    public static class BrokerIdZNode {

        public static String getPath(int brokerId) {
            return BrokerIdsZNode.getPath() + Separator + brokerId;
        }

        public static byte[] encode(BrokerInfo brokerInfo) {
            Map brokerInfoMap = new HashMap();
            brokerInfoMap.put(VersionKey, brokerInfo.getVersion());
            brokerInfoMap.put(HostKey, brokerInfo.getBroker().getHost());
            brokerInfoMap.put(PortKey, brokerInfo.getBroker().getPort());
            brokerInfoMap.put(JmxPortKey, brokerInfo.getJmxPort());
            brokerInfoMap.put(TimestampKey, System.currentTimeMillis());

            return JSON.toJSONString(brokerInfoMap).getBytes();
        }

        public static BrokerInfo decode(int brokerId, byte[] data) {
            JSONObject brokerInfoObj = JSON.parseObject(new String(data));
            String host = brokerInfoObj.getString(HostKey);
            int port = brokerInfoObj.getIntValue(PortKey);
            int version = brokerInfoObj.getIntValue(VersionKey);
            int jmxPort = brokerInfoObj.getIntValue(JmxPortKey);
            return new BrokerInfo(new Broker(brokerId, host, port), version, jmxPort);
        }
    }

    public static class TopicsZNode {

        public static String getPath() {
            return TopicsPath;
        }
    }

    public static class TopicZNode {

        public static String getPath(String topic) {
            return TopicsZNode.getPath() + Separator + topic;
        }

        public static byte[] encode(Map<TopicPartition, List<Integer>> replicas) {
            Map<String, List<Integer>> partitions2replicas = new HashMap<>();
            for (Map.Entry<TopicPartition, List<Integer>> entry : replicas.entrySet()) {
                partitions2replicas.put(String.valueOf(entry.getKey().getPartition()), entry.getValue());
            }

            Map<String, Object> partitionsMap = new HashMap<>();
            partitionsMap.put(VersionKey, 1);
            partitionsMap.put(PartitionsKey, partitions2replicas);
            return JSON.toJSONString(partitionsMap).getBytes();
        }

        public static Map<TopicPartition, List<Integer>> decode(String topic, byte[] data) {
            Map<TopicPartition, List<Integer>> result = new HashMap();
            JSONObject topicObj = JSON.parseObject(new String(data));
            JSONObject partitionsObj = topicObj.getJSONObject(PartitionsKey);
            Set<String> keys = partitionsObj.keySet();
            for (String key : keys) {
                List<Integer> replicaSet = new ArrayList(partitionsObj.getJSONArray(key));
                result.put(new TopicPartition(topic, Integer.parseInt(key)), replicaSet);
            }
            return result;
        }
    }

    public static class TopicPartitionsZNode {
        public static String getPath(String topic) {
            return TopicZNode.getPath(topic) + Separator + PartitionsUrl;
        }
    }

    public static class TopicPartitionZNode {
        public static String getPath(TopicPartition topicPartition) {
            return TopicPartitionsZNode.getPath(topicPartition.getTopic()) + Separator + topicPartition.getPartition();
        }
    }

    public static class TopicPartitionStateZNode {

        private static String PartitionsUrl = "partitions";
        private static String StateUrl = "state";
        private static String ControllerEpochKey = "controller_epoch";
        private static String LeaderKey = "leader";
        private static String VersionKey = "version";
        private static String LeaderEpochKey = "leader_epoch";
        private static String SrKey = "isr";

        public static String getPath(TopicPartition topicPartition) {
            return TopicsZNode.getPath() + Separator + topicPartition.getTopic() + Separator + PartitionsUrl + Separator + topicPartition.getPartition() + Separator + StateUrl;
        }

        public static byte[] encode(LeaderSrAndControllerEpoch leaderSrAndControllerEpoch) {
            Map leaderSrMap = new HashMap();
            leaderSrMap.put(LeaderKey, leaderSrAndControllerEpoch.getLeaderAndSr().getLeader());
            leaderSrMap.put(LeaderEpochKey, leaderSrAndControllerEpoch.getLeaderAndSr().getLeaderEpoch());
            leaderSrMap.put(VersionKey, 1);
            leaderSrMap.put(ControllerEpochKey, leaderSrAndControllerEpoch.getControllerEpoch());
            leaderSrMap.put(SrKey, leaderSrAndControllerEpoch.getLeaderAndSr().getSrReplicas());
            return JSON.toJSONString(leaderSrMap).getBytes();
        }

        public static LeaderSrAndControllerEpoch decode(byte[] data) {
            JSONObject leaderObj = JSON.parseObject(new String(data));
            return new LeaderSrAndControllerEpoch(leaderObj.getIntValue(ControllerEpochKey), new LeaderAndSr(leaderObj.getIntValue(LeaderKey), leaderObj.getIntValue(LeaderEpochKey), new ArrayList(leaderObj.getJSONArray(SrKey))));
        }
    }

    public static class ConfigZNode {
        public static String getPath() {
            return ConfigPath;
        }
    }

    public static class ConfigEntityTypeZNode {
        public static String getPath(String type) {
            return ConfigPath + "/" + type;
        }
    }

    public static class ConfigEntityZNode {
        public static String getPath(String type, String entityName) {
            return ConfigEntityTypeZNode.getPath(type) + "/" + entityName;
        }

        public static byte[] encode(Properties configProperties) {
            Map confgData = new HashMap();
            confgData.put("version", 1);
            confgData.put("config", configProperties);
            return JSON.toJSONString(confgData).getBytes();
        }

        public static Properties decode(String data) {
            JSONObject configData = JSON.parseObject(data);
            JSONObject configProperties = configData.getJSONObject("config");
            Properties properties = new Properties();
            properties.putAll(JSONObject.toJavaObject(configProperties, Map.class));
            return properties;
        }
    }

    public static class AdminZNode {

        public static String getPath() {
            return AdminPath;
        }
    }

    public static class DeleteTopicsZNode {

        public static String getPath() {
            return DeleteTopicsPath;
        }
    }

    public static class ReassignPartitionsZNode {

        public static String getPath() {
            return ReassignPartitionsPath;
        }
    }

    public static class ReassignPartitionsChildZNode {

        public static String getPath(String child) {
            return ReassignPartitionsPath + Separator + child;
        }

        public static Map<TopicPartition, List<Integer>> decode(byte[] data) {
            Map<TopicPartition, List<Integer>> topicPartitionSetMap = new HashMap<>();
            JSONObject reassignPartitionsJson = JSON.parseObject(new String(data));
            JSONArray partitionsSONArray = reassignPartitionsJson.getJSONArray(PartitionsKey);
            for (Object partitionObj : partitionsSONArray) {
                JSONObject partitionJson = (JSONObject) partitionObj;
                String topic = partitionJson.getString(TopicKey);
                int partition = partitionJson.getIntValue(PartitionKey);
                JSONArray replicasJsonArray = partitionJson.getJSONArray(ReplicaKey);
                List<Integer> replicas = new ArrayList(replicasJsonArray);
                topicPartitionSetMap.put(new TopicPartition(topic, partition), replicas);
            }

            return topicPartitionSetMap;
        }

        public static byte[] encode(Map<TopicPartition, List<Integer>> reassignment) {
            Map<String, Object> reassignmentMaps = new HashMap<>();
            List<Map<String, Object>> partitions = new ArrayList<>();
            for (Map.Entry<TopicPartition, List<Integer>> entry : reassignment.entrySet()) {
                Map<String, Object> reassignmentMap = new HashMap<>();
                reassignmentMap.put("topic", entry.getKey().getTopic());
                reassignmentMap.put("partition", entry.getKey().getPartition());
                reassignmentMap.put("replicas", entry.getValue());
                partitions.add(reassignmentMap);
            }
            reassignmentMaps.put("version", 1);
            reassignmentMaps.put("partitions", partitions);
            return JSON.toJSONString(reassignmentMaps).getBytes();
        }
    }

    public static class PreferredReplicaElectionZNode {

        public static String getPath() {
            return PreferredReplicaElection;
        }

        public static byte[] encode(Set<TopicPartition> topicPartitions) {
            Map<String, Object> partitionsMap = new HashMap<>();
            partitionsMap.put(VersionKey, 1);
            partitionsMap.put(PartitionsKey, topicPartitions);
            return JSON.toJSONString(partitionsMap).getBytes();
        }

        public static Set<TopicPartition> decode(byte[] data) {
            Set<TopicPartition> topicPartitions = new HashSet<>();
            JSON topicPartitionsJSON = JSON.parseObject(new String(data));
            JSONArray topicPartitionsArray = ((JSONObject) topicPartitionsJSON).getJSONArray(PartitionsKey);
            for (Object topicPartitionObj : topicPartitionsArray) {
                JSONObject topicPartitionJSON = (JSONObject) topicPartitionObj;
                String topic = topicPartitionJSON.getString(TopicKey);
                int partition = topicPartitionJSON.getIntValue(PartitionKey);
                topicPartitions.add(new TopicPartition(topic, partition));
            }
            return topicPartitions;
        }
    }

    public static class SrChangeNotificationZNode {

        public static String getPath() {
            return SrChangeNotificationPath;
        }
    }

    public static class SrChangeNotificationChildZNode {

        public static String getPath(String child) {
            return SrChangeNotificationPath + Separator + child;
        }

        public static byte[] encode(Set<TopicPartition> topicPartitions) {
            List<Map<String, String>> partitionList = new ArrayList<>();
            for (TopicPartition topicPartition : topicPartitions) {
                Map<String, String> tpMap = new HashMap<>();
                tpMap.put(TopicKey, topicPartition.getTopic());
                tpMap.put(PartitionKey, String.valueOf(topicPartition.getPartition()));
                partitionList.add(tpMap);
            }
            Map<String, Object> map = new HashMap<>();
            map.put(VersionKey, 1);
            map.put(PartitionsKey, partitionList);
            return JSON.toJSONString(map).getBytes();
        }

        public static Set<TopicPartition> decode(byte[] data) {
            Set<TopicPartition> tps = new HashSet<>();
            JSONObject jsonObject = JSON.parseObject(new String(data));
            JSONArray partitions = jsonObject.getJSONArray(PartitionsKey);
            for (Object partition : partitions) {
                JSONObject partitionObj = (JSONObject) partition;
                tps.add(new TopicPartition(partitionObj.getString(TopicKey), partitionObj.getIntValue(PartitionKey)));
            }
            return tps;
        }
    }

    public static class ConfigEntityChangeNotificationSequenceZNode {
        private static final String SequenceNumberPrefix = "config_change_";

        public static String getPath() {
            return ConfigEntityChangeNotificationZNode.getPath() + "/" + SequenceNumberPrefix;
        }

        public static byte[] encode(String sanitizedEntityPath) {
            Map<String, Object> partitionsMap = new HashMap<>();
            partitionsMap.put(VersionKey, 2);
            partitionsMap.put("entity_path", sanitizedEntityPath);
            return JSON.toJSONString(partitionsMap).getBytes();
        }
    }

    public static class BrokerShutdownZNode {
        public static String getPath() {
            return BrokerShutdownPath;
        }
    }

    public static class CheckPointZNode {
        public static String getPath(int partition) {
            return CheckPointPath + Separator + partition;
        }
    }

    public static class ConsumerGroupNode {
        public static String getPath() {
            return ConsumerGroupPath;
        }

        public static String getPathForGroupName(String groupName) {
            return ConsumerGroupPath + Separator + groupName;
        }
    }

    public static class ConfigEntityChangeNotificationZNode {
        public static String getPath() {
            return ConfigEntityChangeNotificationPath;
        }
    }

}