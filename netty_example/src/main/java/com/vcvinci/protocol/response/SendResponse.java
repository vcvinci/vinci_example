package com.vcvinci.protocol.response;

import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月10日 上午6:40:42
 * @description 类说明
 */
public class SendResponse extends AbstractResponse {

    private static final Schema SEND_RESPONSE_V0 = new Schema(
            CommonField.ERROR_CODE,
            CommonField.OFFSET,
            CommonField.STORE_TIME,
            CommonField.DELAY_TIME);

    private long offset;
    private long storeTime;
    private short delayTime;

    public static Schema[] schemaVersions() {
        return new Schema[]{SEND_RESPONSE_V0};
    }

    public SendResponse(short version, short errorCode, long offset, short delayTime) {
        super(version, errorCode);
        this.offset = offset;
        this.delayTime = delayTime;
    }

    public SendResponse(Struct struct, short version) {
        super(version, struct.getShort(ProtocolSchemaConstant.ERROR_CODE_KEY_NAME));
        offset = struct.get(CommonField.OFFSET);
        storeTime = struct.getLong(ProtocolSchemaConstant.STORE_TIME_NAME);
        delayTime = struct.getShort(ProtocolSchemaConstant.DELAY_TIME_NAME);
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.Send.getResponseSchema(getVersion()));
        struct.set(CommonField.ERROR_CODE, error.code());
        struct.set(CommonField.OFFSET, offset);
        struct.set(ProtocolSchemaConstant.STORE_TIME_NAME, storeTime);
        struct.set(ProtocolSchemaConstant.DELAY_TIME_NAME, delayTime);
        return struct;
    }

    public static SendResponse parse(ByteBuffer buffer, short version) {
        return new SendResponse(RequestResponseMapper.Send.parseResponse(version, buffer), version);
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        // TODO Auto-generated method stub
        return null;
    }

    public long getOffset() {
        return offset;
    }

    public long getStoreTime() {
        return storeTime;
    }
}