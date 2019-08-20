package com.vcvinci.common.internal;

import com.vcvinci.common.exception.server.InvalidTopicException;

import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: yuxin.chen01
 * @Date: 2018/7/5 15:09
 * @Description:内部topic的一些信息
 */
public class Topic {

    /**
     * TODO 配置了compact策略，使得它总是能够保存最新的位移信息，既控制了该topic总体的日志容量，也能实现保存最新offset的目的。
     * 而对于每一个日志目录下都有一个名为 " cleaner-offset-checkpoint " 的文件, 这个文件就是清理检查点文件，用来记录每个主题的每个分区中已清理的偏移量
     *
     * 这个topic会保存 GroupMetadata 和 消费分区的 offset 消息：但是这两类消息的key是不同的
     */
    public final static String GROUP_METADATA_TOPIC = "consumer_offset";
    private static final Set<String> INTERNAL_TOPIC_SET = new HashSet<>();
    private static final int MAX_NAME_LENGTH = 249;

    static {
        INTERNAL_TOPIC_SET.add(GROUP_METADATA_TOPIC);
    }

    public static boolean isInternal(String topic) {
        return INTERNAL_TOPIC_SET.contains(topic);
    }

    public static void validate(String topic) {
        if (topic.isEmpty())
            throw new InvalidTopicException("Topic name is illegal, it can't be empty");
        if (topic.equals(".") || topic.equals(".."))
            throw new InvalidTopicException("Topic name cannot be \".\" or \"..\"");
        if (topic.length() > MAX_NAME_LENGTH)
            throw new InvalidTopicException("Topic name is illegal, it can't be longer than " + MAX_NAME_LENGTH +
                    " characters, topic name: " + topic);
        if (!containsValidPattern(topic))
            throw new InvalidTopicException("Topic name \"" + topic + "\" is illegal, it contains a character other than " +
                    "ASCII alphanumerics, '.', '_' and '-'");
    }

    static boolean containsValidPattern(String topic) {
        for (int i = 0; i < topic.length(); ++i) {
            char c = topic.charAt(i);
            // We don't use Character.isLetterOrDigit(c) because it's slower
            boolean validChar = (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || c == '.' ||
                    c == '_' || c == '-';
            if (!validChar)
                return false;
        }
        return true;
    }

    /**
     * 功能描述: <br>
     * 〈Topic名称是否包含非法字符〉
     *
     * @param topic
     * @return > boolean
     * @author HuangTaiHong
     * @date 2018.12.03 10:10:49
     */
    public static boolean hasCollisionChars(String topic) {
        return topic.contains("_") || topic.contains(".");
    }

    /**
     * 功能描述: <br>
     * 〈如果A和B在相同位置有'.'或'_' 则发生碰撞〉
     *
     *  示例: A.B与A_B则发生碰撞
     * @param topicA
     * @param topicB
     * @return > boolean
     * @author HuangTaiHong
     * @date 2018.12.03 10:32:32
     */
    public static boolean hasCollision(String topicA, String topicB) {
        return topicA.replace('.', '_').equals(topicB.replace('.', '_'));
    }
}
