package com.vcvinci.common.partition;

import com.vcvinci.common.cluster.Broker;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月5日 下午1:47:56
 * @description 类说明
 */
public class PartitionInfo {

    private final String topic;
    private final int partition;
    private final Broker leader;
    private final Broker[] replicas;
    private final Broker[] inSyncReplicas;
    private final Broker[] offlineReplicas;

    public PartitionInfo(String topic, int partition, Broker leader, Broker[] replicas, Broker[] inSyncReplicas, Broker[] offlineReplicas) {
        this.topic = topic;
        this.partition = partition;
        this.leader = leader;
        this.replicas = replicas;
        this.inSyncReplicas = inSyncReplicas;
        this.offlineReplicas = offlineReplicas;
    }

    public String getTopic() {
        return topic;
    }

    public int getPartition() {
        return partition;
    }

    public Broker getLeader() {
        return leader;
    }

    public Broker[] getReplicas() {
        return replicas;
    }

    public Broker[] getInSyncReplicas() {
        return inSyncReplicas;
    }

    public Broker[] getOfflineReplicas() {
        return offlineReplicas;
    }

    @Override
    public String toString() {
        return String.format("Partition(topic = %s, partition = %d, leader = %s, replicas = %s, sr = %s, offlineReplicas = %s)",
                topic,
                partition,
                leader == null ? "none" : leader.getId(),
                formatNodeIds(replicas),
                formatNodeIds(inSyncReplicas),
                formatNodeIds(offlineReplicas));
    }

    /**
     * 从数组中的每个项中提取节点id并进行显示
     */
    private String formatNodeIds(Broker[] brokers) {
        StringBuilder b = new StringBuilder("[");
        for (int i = 0; i < brokers.length; i++) {
            b.append(brokers[i].getId());
            if (i < brokers.length - 1) {
                b.append(',');
            }
        }
        b.append("]");
        return b.toString();
    }
}
 