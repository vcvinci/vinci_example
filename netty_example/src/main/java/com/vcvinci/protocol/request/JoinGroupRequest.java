package com.vcvinci.protocol.request;

import com.vcvinci.common.protocol.ProtocolMetadata;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes.ARRAY;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.response.AbstractResponse;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.vcvinci.common.schema.CommonTypes.INT32;
import static com.vcvinci.common.schema.CommonTypes.STRING;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 上午8:19:33
 * @description 类说明
 *
 * <p>
 * 消费加入消费组请求，协调器负责协调组内的消费者
 * </p>
 */
public class JoinGroupRequest extends AbstractRequest {

    private static final Schema JOIN_GROUP_REQUEST_PROTOCOL_V0 = new Schema(
            new Field(ProtocolSchemaConstant.SUPPORTED_PARTITION_ASSIGNOR, STRING));

    // 加入组请求 字段结构 协议对象
    private static final Schema JOIN_GROUP_REQUEST_V0 = new Schema(
            CommonField.GROUP_NAME,
            CommonField.MEMBER_ID,
            new Field(ProtocolSchemaConstant.SESSION_TIMEOUT_KEY_NAME, INT32, "如果在超时（毫秒）之后没有收到心跳，则协调器认为消费者已离线."),
            new Field(ProtocolSchemaConstant.REBALANCE_TIMEOUT_KEY_NAME, INT32, "协调器在重新平衡组时等待每个成员重新加入的最长时间"),
            new Field(ProtocolSchemaConstant.TOPICS_KEY_NAME, new ARRAY<>(STRING)),
            new Field(ProtocolSchemaConstant.GROUP_PROTOCOLS_KEY_NAME, new ARRAY<>(JOIN_GROUP_REQUEST_PROTOCOL_V0), "List of protocols that the member supports"));

    public static final String UNKNOWN_MEMBER_ID = "";

    // 消费组名称
    private String groupName;
    // 消费组成员编号
    private String memberId;
    private int sessionTimeout;
    private int rebalanceTimeout;
    private List<String> topics;
    private List<ProtocolMetadata> protocols;

    // 不记录协议参数中，由服务端通过 channel 获取
    private String clientHost;

    public JoinGroupRequest(short version, String groupName, String memberId, int sessionTimeout, int rebalanceTimeout,
                            List<String> topics, List<ProtocolMetadata> protocols) {
        super(RequestResponseMapper.JoinGroup, version);
        this.groupName = groupName;
        this.memberId = memberId;
        this.sessionTimeout = sessionTimeout;
        this.rebalanceTimeout = rebalanceTimeout;
        this.protocols = protocols;
        this.topics = topics;
    }

    public JoinGroupRequest(final Struct struct, final short version) {
        super(RequestResponseMapper.JoinGroup, version);
        this.groupName = struct.get(CommonField.GROUP_NAME);
        this.memberId = struct.get(CommonField.MEMBER_ID);
        this.sessionTimeout = struct.getInt(ProtocolSchemaConstant.SESSION_TIMEOUT_KEY_NAME);
        this.rebalanceTimeout = struct.getInt(ProtocolSchemaConstant.REBALANCE_TIMEOUT_KEY_NAME);
        String[] topicArray = struct.getStringArray(ProtocolSchemaConstant.TOPICS_KEY_NAME);
        topics = new ArrayList<>(Arrays.asList(topicArray));
        Struct[] groupProtocolArray = struct.getStructArray(ProtocolSchemaConstant.GROUP_PROTOCOLS_KEY_NAME);

        protocols = new ArrayList<>();
        for (Struct groupProtocolStruct : groupProtocolArray) {
            String name = groupProtocolStruct.getString(ProtocolSchemaConstant.SUPPORTED_PARTITION_ASSIGNOR);
            protocols.add(new ProtocolMetadata(name));
        }
    }

    public static Schema[] schemaVersions() {
        return new Schema[]{JOIN_GROUP_REQUEST_V0};
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.JoinGroup.getRequestSchema(getVersion()));
        struct.set(CommonField.GROUP_NAME, groupName);
        struct.set(CommonField.MEMBER_ID, memberId);
        struct.set(ProtocolSchemaConstant.SESSION_TIMEOUT_KEY_NAME, sessionTimeout);
        struct.set(ProtocolSchemaConstant.REBALANCE_TIMEOUT_KEY_NAME, rebalanceTimeout);
        struct.set(ProtocolSchemaConstant.TOPICS_KEY_NAME, topics.toArray());
        List<Struct> groupProtocolsList = new ArrayList<>(protocols.size());
        for (ProtocolMetadata protocol : protocols) {
            Struct protocolStruct = struct.instance(ProtocolSchemaConstant.GROUP_PROTOCOLS_KEY_NAME);
            protocolStruct.set(ProtocolSchemaConstant.SUPPORTED_PARTITION_ASSIGNOR, protocol.getName());
            groupProtocolsList.add(protocolStruct);
        }
        struct.set(ProtocolSchemaConstant.GROUP_PROTOCOLS_KEY_NAME, groupProtocolsList.toArray());
        return struct;
    }

    @Override
    public AbstractResponse getErrorResponse(int throttleTime, Throwable e) {
        // TODO Auto-generated method stub
        return null;
    }
    public static JoinGroupRequest parse(ByteBuffer buffer, short version) {
        return new JoinGroupRequest(RequestResponseMapper.JoinGroup.parseRequest(version, buffer), version);
    }

    public String groupName() {
        return this.groupName;
    }

    public List<ProtocolMetadata> protocols() {
        return this.protocols;
    }

    public int sessionTimeout() {
        return sessionTimeout;
    }

    public String memberId() {
        return memberId;
    }

    public int rebalanceTimeout() {
        return this.rebalanceTimeout;
    }

    public List<String> topics() {
        return this.topics;
    }

    public String getClientHost() {
        return clientHost;
    }

    public void setClientHost(String clientHost) {
        this.clientHost = clientHost;
    }

    @Override
    public String toString() {
        return "JoinGroupRequest{"
                + "groupName='" + groupName + '\''
                + ", memberId='" + memberId + '\''
                + ", sessionTimeout=" + sessionTimeout
                + ", rebalanceTimeout=" + rebalanceTimeout
                + ", topics=" + topics
                + ", protocols=" + protocols
                + ", clientHost='" + clientHost + '\''
                + '}';
    }
}
