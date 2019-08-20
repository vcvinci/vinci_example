package com.vcvinci.protocol.util;

import com.vcvinci.common.partition.TopicPartition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年7月2日 下午3:32:03
 * @description 类说明
 */
public class CollectionUtils {

    public static <T> Map<String, Map<Integer, T>> groupDataByTopic(Map<TopicPartition, ? extends T> data) {
        Map<String, Map<Integer, T>> dataByTopic = new HashMap<>();
        for (Map.Entry<TopicPartition, ? extends T> entry : data.entrySet()) {
            String topic = entry.getKey().getTopic();
            int partition = entry.getKey().getPartition();
            Map<Integer, T> topicData = dataByTopic.get(topic);
            if (topicData == null) {
                topicData = new HashMap<>();
                dataByTopic.put(topic, topicData);
            }
            topicData.put(partition, entry.getValue());
        }
        return dataByTopic;
    }

    public static Map<String, List<Integer>> groupDataByTopic(List<TopicPartition> partitions) {
        Map<String, List<Integer>> partitionsByTopic = new HashMap<>();
        for (TopicPartition tp : partitions) {
            String topic = tp.getTopic();
            List<Integer> topicData = partitionsByTopic.get(topic);
            if (topicData == null) {
                topicData = new ArrayList<>();
                partitionsByTopic.put(topic, topicData);
            }
            topicData.add(tp.getPartition());
        }
        return partitionsByTopic;
    }

    /**
     * 可以 迁移到 工具类中
     *
     * @param keySet
     * @param value
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> mapValues(Collection<K> keySet, V value) {
        if (keySet == null || keySet.size() == 0) {
            return Collections.emptyMap();
        }
        final Map<K, V> resultMap = new HashMap<>(keySet.size());
        for (K key : keySet) {
            resultMap.put(key, value);
        }
        return resultMap;
    }

    public static <K, V> Map<K, V> mapValues(Map<K, ?> originalMap, V value) {
        if (originalMap == null || originalMap.size() == 0) {
            return Collections.emptyMap();
        }
        Map<K, V> resultMap = new HashMap<>(originalMap.size());
        for (K key : originalMap.keySet()) {
            resultMap.put(key, value);
        }
        return resultMap;
    }
}
