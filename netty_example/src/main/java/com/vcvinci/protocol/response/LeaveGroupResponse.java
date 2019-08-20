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
 * @version 创建时间：2018年5月9日 下午4:28:07
 * @description 类说明
 */
public class LeaveGroupResponse extends AbstractResponse {

    private static final Schema LEAVE_GROUP_RESPONSE_V0 = new Schema(CommonField.ERROR_CODE);

    public static Schema[] schemaVersions() {
        return new Schema[]{LEAVE_GROUP_RESPONSE_V0};
    }

    public LeaveGroupResponse(short version, ErrorCodes error) {
        super(version, error);
    }

    public LeaveGroupResponse(Struct struct, short version) {
        super(version, struct.get(CommonField.ERROR_CODE));
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.LeaveGroup.getResponseSchema(getVersion()));
        struct.set(ProtocolSchemaConstant.ERROR_CODE_KEY_NAME, getError().code());
        return struct;
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        // TODO Auto-generated method stub
        return null;
    }

    public static LeaveGroupResponse parse(ByteBuffer buffer, short version) {
        return new LeaveGroupResponse(RequestResponseMapper.LeaveGroup.parseResponse(version, buffer), version);
    }
}
