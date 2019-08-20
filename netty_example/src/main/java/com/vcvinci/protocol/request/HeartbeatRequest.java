package com.vcvinci.protocol.request;

import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.response.AbstractResponse;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 上午8:18:25
 * @description 类说明
 */
public class HeartbeatRequest extends AbstractRequest {

    private static final Schema HEARTBEAT_REQUEST_V0 = new Schema(
            new Field(ProtocolSchemaConstant.METADATA_VERSION_KEY_NAME, CommonTypes.INT32),
            CommonField.GROUP_NAME,
            CommonField.GENERATION_ID,
            CommonField.MEMBER_ID);

    private final String groupName;
    private final String memberId;
    private final int generationId;
    /**
     * 元数据版本号, 通过它可以和元数据缓存中的版本号对比，如果不一致，则心跳响应时需要带上当前最新的元数据版本和元数据内容
     */
    private final int metadataVersion;

    public static Schema[] schemaVersions() {
        return new Schema[]{HEARTBEAT_REQUEST_V0};
    }

    public HeartbeatRequest(short version, String groupName, String memberId, int generationId, int metadataVersion) {
        super(RequestResponseMapper.Heartbeat, version);
        this.groupName = groupName;
        this.memberId = memberId;
        this.generationId = generationId;
        this.metadataVersion = metadataVersion;
    }

    public HeartbeatRequest(Struct struct, short version) {
        super(RequestResponseMapper.Heartbeat, version);
        this.groupName = struct.get(CommonField.GROUP_NAME);
        this.memberId = struct.get(CommonField.MEMBER_ID);
        this.generationId = struct.get(CommonField.GENERATION_ID);
        this.metadataVersion = struct.getInt(ProtocolSchemaConstant.METADATA_VERSION_KEY_NAME);
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.Heartbeat.getRequestSchema(getVersion()));
        struct.set(CommonField.GROUP_NAME, groupName);
        struct.set(CommonField.GENERATION_ID, generationId);
        struct.set(CommonField.MEMBER_ID, memberId);
        struct.set(ProtocolSchemaConstant.METADATA_VERSION_KEY_NAME, metadataVersion);
        return struct;
    }

    @Override
    public AbstractResponse getErrorResponse(int throttleTime, Throwable e) {
        return null;
    }

    public static HeartbeatRequest parse(ByteBuffer buffer, short version) {
        return new HeartbeatRequest(RequestResponseMapper.Heartbeat.parseRequest(version, buffer), version);
    }

    public String groupName() {
        return this.groupName;
    }

    public String memberId() {
        return this.memberId;
    }

    public int generation() {
        return generationId;
    }

    public int getMetadataVersion() {
        return metadataVersion;
    }
}
