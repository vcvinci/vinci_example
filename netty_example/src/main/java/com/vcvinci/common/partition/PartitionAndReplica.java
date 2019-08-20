package com.vcvinci.common.partition;

import java.util.Objects;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月2日 下午8:45:36
 * @description 类说明
 */
public class PartitionAndReplica {

    private TopicPartition topicPartition;
    private int replica;

    public PartitionAndReplica(TopicPartition topicPartition, int replica) {
        this.topicPartition = topicPartition;
        this.replica = replica;
    }

    public TopicPartition getTopicPartition() {
        return topicPartition;
    }

    public void setTopicPartition(TopicPartition topicPartition) {
        this.topicPartition = topicPartition;
    }

    public int getReplica() {
        return replica;
    }

    public void setReplica(int replica) {
        this.replica = replica;
    }

    @Override
    public String toString() {
        return "PartitionAndReplica{" +
                "topicPartition=" + topicPartition +
                ", replica=" + replica +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartitionAndReplica that = (PartitionAndReplica) o;
        return replica == that.replica &&
                Objects.equals(topicPartition, that.topicPartition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topicPartition, replica);
    }
}
