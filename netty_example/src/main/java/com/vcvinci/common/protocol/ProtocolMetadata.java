package com.vcvinci.common.protocol;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 上午8:21:45
 * @description 类说明
 */
public class ProtocolMetadata {

    /**
     * 协议名称
     * {@link com.ucarinc.dove.client.consumer.protocol.PartitionAssignor#name()}
     */
    private String name;

    public ProtocolMetadata(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProtocolMetadata{"
                + "name='" + name + '\''
                + '}';
    }
}
 