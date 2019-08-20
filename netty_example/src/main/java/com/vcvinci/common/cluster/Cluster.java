package com.vcvinci.common.cluster;

import com.vcvinci.common.partition.PartitionInfo;
import com.vcvinci.common.partition.TopicPartition;
import org.apache.commons.lang3.Validate;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月5日 下午1:43:04
 * @description 类说明
 */
public class Cluster {

    private final boolean isBootstrapConfigured;

    /**
     * 集群节点信息列表
     */
    private List<Broker> brokers;
    /**
     * 集群中唯一控制器
     */
    private Broker controller;
    /**
     * 当前集群有哪些是未授权的topic
     */
    private Set<String> unauthorizedTopics;
    /**
     * 当前集群有哪些内部的topic，比如offsets topic
     */
    private Set<String> internalTopics;
    private Map<TopicPartition, PartitionInfo> tp2PartitionInfo;
    private Map<String, List<PartitionInfo>> topic2PartitonInfos;
    private Map<String, List<PartitionInfo>> topic2AvailablePartitonInfos;
    private Map<Integer, List<PartitionInfo>> broker2PartiitonInfos;
    private Map<Integer, Broker> id2Brokers;
    private String clusterId;

    public Cluster(String clusterId,
                   Collection<Broker> brokers,
                   Collection<PartitionInfo> partitions,
                   Set<String> unauthorizedTopics,
                   Set<String> internalTopics) {
        this(clusterId, false, brokers, partitions, unauthorizedTopics, internalTopics, null);
    }

    public Cluster(String clusterId,
                   Collection<Broker> nodes,
                   Collection<PartitionInfo> partitions,
                   Set<String> unauthorizedTopics,
                   Set<String> internalTopics,
                   Broker controller) {
        this(clusterId, false, nodes, partitions, unauthorizedTopics, internalTopics, controller);
    }

    private Cluster(String clusterId,
                    boolean isBootstrapConfigured,
                    Collection<Broker> nodes,
                    Collection<PartitionInfo> partitions,
                    Set<String> unauthorizedTopics,
                    Set<String> internalTopics,
                    Broker controller) {
        this.clusterId = clusterId;
        this.isBootstrapConfigured = isBootstrapConfigured;
        // 创建一个随机的、不可修改的节点副本
        List<Broker> copy = new ArrayList<>(nodes);
        Collections.shuffle(copy);
        this.brokers = Collections.unmodifiableList(copy);
        this.id2Brokers = new HashMap<>();
        for (Broker broker : nodes) {
            this.id2Brokers.put(broker.getId(), broker);
        }

        // 按主题/分区索引分区以便快速查找
        this.tp2PartitionInfo = new HashMap<>(partitions.size());
        for (PartitionInfo p : partitions) {
            this.tp2PartitionInfo.put(new TopicPartition(p.getTopic(), p.getPartition()), p);
        }

        // 按主题和代理分别索引分区，并创建列表
        // 不可修改，这样我们就可以在面向用户的api中分发它们而不产生任何风险修改内容的客户端
        HashMap<String, List<PartitionInfo>> partsForTopic = new HashMap<>();
        HashMap<Integer, List<PartitionInfo>> partsForNode = new HashMap<>();
        for (Broker n : this.brokers) {
            partsForNode.put(n.getId(), new ArrayList<PartitionInfo>());
        }
        for (PartitionInfo p : partitions) {
            if (!partsForTopic.containsKey(p.getTopic())) {
                partsForTopic.put(p.getTopic(), new ArrayList<PartitionInfo>());
            }
            List<PartitionInfo> psTopic = partsForTopic.get(p.getTopic());
            psTopic.add(p);

            if (p.getLeader() != null) {
                List<PartitionInfo> psNode = Validate.notNull(partsForNode.get(p.getLeader().getId()));
                psNode.add(p);
            }
        }
        this.topic2PartitonInfos = new HashMap<>(partsForTopic.size());
        this.topic2AvailablePartitonInfos = new HashMap<>(partsForTopic.size());
        for (Map.Entry<String, List<PartitionInfo>> entry : partsForTopic.entrySet()) {
            String topic = entry.getKey();
            List<PartitionInfo> partitionList = entry.getValue();
            this.topic2PartitonInfos.put(topic, Collections.unmodifiableList(partitionList));
            List<PartitionInfo> availablePartitions = new ArrayList<>();
            for (PartitionInfo part : partitionList) {
                if (part.getLeader() != null) {
                    availablePartitions.add(part);
                }
            }
            this.topic2AvailablePartitonInfos.put(topic, Collections.unmodifiableList(availablePartitions));
        }
        this.broker2PartiitonInfos = new HashMap<>(partsForNode.size());
        for (Map.Entry<Integer, List<PartitionInfo>> entry : partsForNode.entrySet()) {
            this.broker2PartiitonInfos.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
        }

        this.unauthorizedTopics = Collections.unmodifiableSet(unauthorizedTopics);
        this.internalTopics = Collections.unmodifiableSet(internalTopics);
        this.controller = controller;
    }

    /**
     * 创建一个没有节点和topic-partitions得空集群实例
     */
    public static Cluster empty() {
        return new Cluster(null, new ArrayList<Broker>(0), new ArrayList<PartitionInfo>(0), Collections.<String>emptySet(),
                Collections.<String>emptySet(), null);
    }

    /**
     * 用地址创建一个引导集群
     *
     * @param addresses The addresses
     * @return Cluster
     */
    public static Cluster bootstrap(List<InetSocketAddress> addresses) {
        List<Broker> brokers = new ArrayList<>();
        int nodeId = -1;
        for (InetSocketAddress address : addresses) {
            brokers.add(new Broker(nodeId--, address.getHostString(), address.getPort()));
        }
        return new Cluster(null, true, brokers, new ArrayList<PartitionInfo>(0), Collections.<String>emptySet(), Collections.<String>emptySet(), null);
    }

    /**
     * 返回该集群的副本和“分区”。
     */
    public Cluster withPartitions(Map<TopicPartition, PartitionInfo> partitions) {
        Map<TopicPartition, PartitionInfo> combinedPartitions = new HashMap<>(this.tp2PartitionInfo);
        combinedPartitions.putAll(partitions);
        return new Cluster(this.clusterId, this.brokers, combinedPartitions.values(),
                new HashSet<>(this.unauthorizedTopics), new HashSet<>(this.internalTopics), this.controller);
    }

    public List<Broker> getBrokers() {
        return this.brokers;
    }


    /**
     * 获取给定主题分区所在的broker节点
     */
    public Broker leaderFor(TopicPartition topicPartition) {
        PartitionInfo info = tp2PartitionInfo.get(topicPartition);
        if (info == null) {
            return null;
        } else {
            return info.getLeader();
        }
    }

    /**
     * 获取指定分区的元数据
     */
    public PartitionInfo partition(TopicPartition topicPartition) {
        return tp2PartitionInfo.get(topicPartition);
    }

    public List<PartitionInfo> partitionsForTopic(String topic) {
        List<PartitionInfo> parts = this.topic2PartitonInfos.get(topic);
        return (parts == null) ? Collections.<PartitionInfo>emptyList() : parts;
    }

    public List<TopicPartition> getTopicPartitionsByTopic(String topic) {
        List<PartitionInfo> partitionInfos = partitionsForTopic(topic);
        if (partitionInfos.isEmpty()) {
            return Collections.<TopicPartition>emptyList();
        }
        List<TopicPartition> resultList = new ArrayList<TopicPartition>();
        for (PartitionInfo info : partitionInfos) {
            resultList.add(Cluster.buildTopicPartition(topic, info.getPartition()));
        }
        return resultList;
    }

    public static TopicPartition buildTopicPartition(String topic, int partition) {
        return new TopicPartition(topic, partition);
    }

    public Integer partitionCountForTopic(String topic) {
        List<PartitionInfo> partitions = this.topic2PartitonInfos.get(topic);
        return partitions == null ? null : partitions.size();
    }

    public List<PartitionInfo> availablePartitionsForTopic(String topic) {
        List<PartitionInfo> parts = this.topic2AvailablePartitonInfos.get(topic);
        return (parts == null) ? Collections.<PartitionInfo>emptyList() : parts;
    }

    public List<PartitionInfo> partitionsForNode(int nodeId) {
        List<PartitionInfo> parts = this.broker2PartiitonInfos.get(nodeId);
        return (parts == null) ? Collections.<PartitionInfo>emptyList() : parts;
    }

    public List<PartitionInfo> partitionsForTopics(Set<String> topics){
        List<PartitionInfo> partitionInfoSet = new ArrayList<>();
        for(String topic: topics){
            List<PartitionInfo> partitionInfos = this.topic2PartitonInfos.get(topic);
            partitionInfoSet.addAll(partitionInfos);
        }
        return partitionInfoSet;
    }

    public Set<String> topics() {
        return this.topic2PartitonInfos.keySet();
    }

    public Set<String> unauthorizedTopics() {
        return unauthorizedTopics;
    }

    public Set<String> internalTopics() {
        return internalTopics;
    }

    public boolean isBootstrapConfigured() {
        return isBootstrapConfigured;
    }

    public String getClusterId() {
        return clusterId;
    }

    public Broker controller() {
        return controller;
    }

    @Override
    public String toString() {
        return "Cluster(id = " + this.clusterId + ", nodes = " + this.brokers + ", partitions = " + this.tp2PartitionInfo.values() + ")";
    }

}
