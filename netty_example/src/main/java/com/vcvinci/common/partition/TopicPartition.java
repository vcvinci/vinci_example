package com.vcvinci.common.partition;

import com.ucarinc.dove.util.FormatUtil;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月2日 下午8:34:10
 * @description 类说明
 */
public final class TopicPartition implements Serializable {

    private static final long serialVersionUID = 4456489934187816658L;

    private int partition;

    private String topic;

    public TopicPartition(String topic, int partition) {
        this.partition = partition;
        this.topic = topic;
    }

    public static TopicPartition fromString(String tp) {
        String[] parts = FormatUtil.parsePartition(tp);
        if (parts == null) {
            return null;
        }
        String topic = parts[0];
        int partition;
        try {
            partition = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return null;
        }
        return new TopicPartition(topic, partition);
    }

    public int getPartition() {
        return this.partition;
    }

    public String getTopic() {
        return this.topic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.topic, this.partition);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TopicPartition)) {
            return false;
        }
        return Objects.equals(this.topic, ((TopicPartition) obj).topic)
                && this.partition == ((TopicPartition) obj).partition;
    }

    @Override
    public String toString() {
        return FormatUtil.partition(this.topic, this.partition);
    }
}
