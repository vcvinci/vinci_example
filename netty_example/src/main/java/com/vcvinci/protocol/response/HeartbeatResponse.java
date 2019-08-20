package com.vcvinci.protocol.response;

import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;
import java.util.Map;

import static com.vcvinci.common.schema.CommonTypes.NULLABLE_DATA;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 下午4:13:45
 * @description 类说明
 */
public class HeartbeatResponse extends AbstractResponse {

    private static final Schema HEARTBEAT_RESPONSE_V0 = new Schema(
            CommonField.ERROR_CODE,
            new Field(ProtocolSchemaConstant.METADATA_KEY_NAME, NULLABLE_DATA, "元数据")
    );

    public static Schema[] schemaVersions() {
        return new Schema[]{HEARTBEAT_RESPONSE_V0};
    }

    /**
     * 就是 MetadataResponse -> Struct
     * 每次心跳时，如果检测到元数据版本未发生变化， 返回空
     * <p>
     * 可以使用：MetadataResponse.parse(ByteBuffer buffer) 进行解析
     */
    private ByteBuffer metadataByteBuffer;

    public HeartbeatResponse(short version, ErrorCodes errorCode, ByteBuffer metadataByteBuffer) {
        super(version, errorCode);
        this.metadataByteBuffer = metadataByteBuffer;
    }

    public HeartbeatResponse(Struct struct, short version) {
        super(version, struct.get(CommonField.ERROR_CODE));
        this.metadataByteBuffer = struct.getByteBuffer(ProtocolSchemaConstant.METADATA_KEY_NAME);
    }

    public ByteBuffer metadata() {
        return metadataByteBuffer;
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.Heartbeat.getResponseSchema(getVersion()));
        struct.set(CommonField.ERROR_CODE, getError().code());
        struct.set(ProtocolSchemaConstant.METADATA_KEY_NAME, metadataByteBuffer);
        return struct;
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        return null;
    }

    public static HeartbeatResponse parse(ByteBuffer buffer, short version) {
        return new HeartbeatResponse(RequestResponseMapper.Heartbeat.parseResponse(version, buffer), version);
    }

}