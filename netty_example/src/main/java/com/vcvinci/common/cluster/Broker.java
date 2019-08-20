package com.vcvinci.common.cluster;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月3日 上午7:47:19
 * @description 类说明
 */
public class Broker {

    /**
     * @param brokerId brokerId
     * @return <pre>brokerId</pre>是否合法
     */
    public static boolean isValid(int brokerId) {
        return brokerId >= 0;
    }

    private static final Broker NO_BROKER = new Broker(-1, "", -1);

    private final int id;
    private final String host;
    private final int port;

    public Broker(int id, String host, int port) {
        super();
        this.id = id;
        this.host = host;
        this.port = port;
    }

    public static Broker noBroker() {
        return NO_BROKER;
    }

    public boolean isEmpty() {
        return host == null || host.isEmpty() || port < 0;
    }

    public int getId() {
        return id;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String addr() {
        return host + ":" + port;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + id;
        result = prime * result + port;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Broker other = (Broker) obj;
        if (host == null) {
            if (other.host != null) {
                return false;
            }
        } else if (!host.equals(other.host)) {
            return false;
        }
        if (id != other.id) {
            return false;
        }
        if (port != other.port) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return host + ":" + port + " (id: " + id + ")";
    }
}
