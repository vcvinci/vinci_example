package com.vcvinci.protocol.request;

import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes;
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

/**
 * 类说明: metadata请求格式
 * @author vinci
 * @version 创建时间：2018年5月4日 上午8:32:46
 */
public class MetadataRequest extends AbstractRequest {

    private List<String> topics;
    private boolean allowAutoTopicCreation;
    private int controllerEpoch = 0;
    private int metadataVersion = 0;
    private static final Schema METADATA_REQUEST_V0 = new Schema(
            CommonField.CONTROLLER_EPOCH,
            CommonField.METADATA_VERSION,
            new Field(ProtocolSchemaConstant.TOPICS_KEY_NAME, new ARRAY<>(CommonTypes.STRING), "An array of topics to fetch metadata for. If no topics are specified fetch metadata for all topics."),
            new Field(ProtocolSchemaConstant.ALLOW_AUTO_TOPIC_CREATION_KEY_NAME, CommonTypes.BOOLEAN, "If this and the broker config " +
                    "'auto.create.topics.enable' are true, topics that don't exist will be created by the broker. " +
                    "Otherwise, no topics will be created by the broker."));

    public MetadataRequest(List<String> topics, boolean allowAutoTopicCreation) {
        this(RequestResponseMapper.Metadata.minVersion(), topics, allowAutoTopicCreation);
    }

    public MetadataRequest(short version, List<String> topics, boolean allowAutoTopicCreation) {
        super(RequestResponseMapper.Metadata, version);
        this.topics = topics;
        this.allowAutoTopicCreation = allowAutoTopicCreation;
    }

    public MetadataRequest(Struct struct, short version) {
        super(RequestResponseMapper.Metadata, version);
        this.topics = new ArrayList<>(Arrays.asList((String[]) struct.get(ProtocolSchemaConstant.TOPICS_KEY_NAME)));
        this.allowAutoTopicCreation = (boolean) struct.get(ProtocolSchemaConstant.ALLOW_AUTO_TOPIC_CREATION_KEY_NAME);
        this.controllerEpoch = struct.getInt(ProtocolSchemaConstant.CONTROLLER_EPOCH_KEY_NAME);
        this.metadataVersion = struct.getInt(ProtocolSchemaConstant.METADATA_VERSION_KEY_NAME);
    }

    public static Schema[] schemaVersions() {
        return new Schema[]{METADATA_REQUEST_V0};
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.Metadata.getRequestSchema(getVersion()));
        struct.set(ProtocolSchemaConstant.CONTROLLER_EPOCH_KEY_NAME, controllerEpoch);
        struct.set(ProtocolSchemaConstant.METADATA_VERSION_KEY_NAME, metadataVersion);
        struct.set(ProtocolSchemaConstant.TOPICS_KEY_NAME, topics.toArray());
        struct.set(ProtocolSchemaConstant.ALLOW_AUTO_TOPIC_CREATION_KEY_NAME, allowAutoTopicCreation);
        return struct;
    }

    @Override
    public AbstractResponse getErrorResponse(int throttleTime, Throwable e) {
        // TODO Auto-generated method stub
        return null;
    }

    public static MetadataRequest parse(ByteBuffer buffer, short version) {
        return new MetadataRequest(RequestResponseMapper.Metadata.parseRequest(version, buffer), version);
    }

    public List<String> topics() {
        return this.topics;
    }

    public int controllerEpoch() {
        return controllerEpoch;
    }

    public int metadataVersion() {
        return metadataVersion;
    }

    @Override
    public String toString() {
        return "MetadataRequest{"
                + "topics=" + topics
                + ", allowAutoTopicCreation=" + allowAutoTopicCreation
                + '}';
    }
}
 