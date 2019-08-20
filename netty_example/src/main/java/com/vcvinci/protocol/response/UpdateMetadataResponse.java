package com.vcvinci.protocol.response;

import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月23日 上午11:38:49
 * @description 类说明
 */
public class UpdateMetadataResponse extends AbstractResponse {

    private static final Schema UPDATE_METADATA_RESPONSE_V0 = new Schema(CommonField.ERROR_CODE);

    public UpdateMetadataResponse(ErrorCodes error, short version) {
        super(version, error);
    }

    public UpdateMetadataResponse(Struct struct, short version) {
        super(version, struct.get(CommonField.ERROR_CODE));
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.UpdateMetadata.getResponseSchema(getVersion()));
        struct.set(CommonField.ERROR_CODE, getError().code());
        return struct;
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        // TODO Auto-generated method stub
        return null;
    }

    public static UpdateMetadataResponse parse(ByteBuffer buffer, short version) {
        return new UpdateMetadataResponse(RequestResponseMapper.UpdateMetadata.parseResponse(version, buffer), version);
    }

    public static Schema[] schemaVersions() {
        return new Schema[]{UPDATE_METADATA_RESPONSE_V0};
    }

}
