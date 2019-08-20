package com.vcvinci.common.message;

public class MessageHelper {

    public static void setMessageId(Message message, long id) {
        message.setId(id);
    }

    public static void setTopic(Message message, String topic) {
        message.setTopic(topic);
    }

    public static void setPartitionId(Message message, int partitionId) {
        message.setPartition(partitionId);
    }

    public static void setClientTime(Message message, long clientTime) {
        message.setClientTime(clientTime);
    }

    public static void setStoreTime(Message message, long storeTime) {
        message.setStoreTime(storeTime);
    }
}
