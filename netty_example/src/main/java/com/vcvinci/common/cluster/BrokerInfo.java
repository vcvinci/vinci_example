package com.vcvinci.common.cluster;

import com.vcvinci.common.util.zookeeper.ZkData;

/**
 * @author zhixing.wang E-mail:zhixing.wang@ucarinc.com
 * @version 创建时间：2018/12/10 11:36
 * @description BrokerInfo
 */
public class BrokerInfo {
    private Broker broker;
    private int version;
    private int jmxPort;

    public BrokerInfo(Broker broker, int version, int jmxPort) {
        this.broker = broker;
        this.version = version;
        this.jmxPort = jmxPort;
    }

    public String getPath() {
        return ZkData.BrokerIdZNode.getPath(broker.getId());
    }

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getJmxPort() {
        return jmxPort;
    }

    public void setJmxPort(int jmxPort) {
        this.jmxPort = jmxPort;
    }
}
