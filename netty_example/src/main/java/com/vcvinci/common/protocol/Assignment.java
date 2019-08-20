package com.vcvinci.common.protocol;

import com.vcvinci.common.partition.TopicPartition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 上午8:29:08
 * @description 类说明
 */
public class Assignment {

    public static final Assignment EMPTY_ASSIGNMENT = new Assignment(new ArrayList<TopicPartition>(0));

    private List<TopicPartition> topicPartitionList;

    public Assignment(List<TopicPartition> topicPartitionList) {
        this.topicPartitionList = topicPartitionList;
    }

    public List<TopicPartition> getTopicPartitionList() {
        return topicPartitionList;
    }

    public void setTopicPartitionList(List<TopicPartition> topicPartitionList) {
        this.topicPartitionList = topicPartitionList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assignment)) return false;
        Assignment that = (Assignment) o;
        return Objects.equals(getTopicPartitionList(), that.getTopicPartitionList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTopicPartitionList());
    }

    @Override
    public String toString() {
        return "Assignment{"
                + "topicPartitionList=" + topicPartitionList
                + '}';
    }
}
