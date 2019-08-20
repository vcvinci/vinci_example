package com.vcvinci.common.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 发送的消息的基类
 * @author yupeng.sun@ucarinc.com
 * @version 1.0 创建时间：2017-12-20 上午9:37:08
 */
public class Message<K,V> implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String topic;
	private int partition;
	private long offset;
	private byte flag;
	private long clientTime;
	private long storeTime;
	private Map<String, String> properties;
	private K key;
	private V body;

	public Message(String topic, V body) {
		this(-1, topic, -1, -1, (byte)0, -1, -1, null, null, body);
	}

	public Message(String topic, K key, V body) {
		this(-1, topic, -1, -1, (byte)0, -1, -1, null, key, body);
	}

	public Message(String topic, int partition, int offset, K key,  V body) {
		this(-1, topic, partition, offset, (byte)0, -1, -1, null, key, body);
	}

	public Message(long id, String topic, int partition, long offset, byte flag, long clientTime, long storeTime, Map<String, String> properties, K key, V body) {
		this.id = id;
		this.topic = topic;
		this.partition = partition;
		this.offset = offset;
		this.flag = flag;
		this.clientTime = clientTime;
		this.storeTime = storeTime;
		this.properties = properties;
		this.key = key;
		this.body = body;
	}

	public void setKeys(String keys) {
        this.putProperty(MessageConstant.PROPERTY_KEY, keys);
    }
	
    void putProperty(final String name, final String value) {
        if (null == this.properties) {
            this.properties = new HashMap<String, String>();
        }

        this.properties.put(name, value);
    }

    public String getKeys(){
		return this.getProperty(MessageConstant.PROPERTY_KEY);
	}
    
    String getProperty(final String name){
    	if(null == this.properties){
    		return null;
    	}
    	return this.properties.get(name);
    }

	public long getId() {
		return id;
	}

	void setId(long id) {
		this.id = id;
	}

	public String getTopic() {
		return topic;
	}

	void setTopic(String topic) {
		this.topic = topic;
	}

	public int getPartition() {
		return this.partition;
	}

	void setPartition(int partition) {
		this.partition = partition;
	}

	public byte getFlag() {
		return flag;
	}

	void setFlag(byte flag) {
		this.flag = flag;
	}

	public long getClientTime() {
		return clientTime;
	}

	void setClientTime(long clientTime) {
		this.clientTime = clientTime;
	}

	public long getStoreTime() {
		return storeTime;
	}

	void setStoreTime(long storeTime) {
		this.storeTime = storeTime;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public void setBody(V body) {
		this.body = body;
	}

	public K getKey() {
		return key;
	}

	public  V getBody() {
		return body;
	}

	public long getOffset() {
		return offset;
	}
}
