package com.vcvinci.protocol.request;

import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.protocol.response.AbstractResponse;
import com.vcvinci.protocol.util.RequestResponseMapper;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月5日 上午10:53:52
 * @description 发送API版本请求
 */
public class ApiVersionRequest extends AbstractRequest {

    private static final Schema API_VERSION_REQUEST_V0 = new Schema();

    public ApiVersionRequest() {
        super(RequestResponseMapper.ApiVersion, RequestResponseMapper.ApiVersion.minVersion());
    }

    public ApiVersionRequest(short version) {
        super(RequestResponseMapper.ApiVersion, version);
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.ApiVersion.getRequestSchema(getVersion()));
        return struct;
    }

    public static Schema[] schemaVersions() {
        return new Schema[]{API_VERSION_REQUEST_V0};
    }

    @Override
    public AbstractResponse getErrorResponse(int throttleTime, Throwable e) {
        // TODO Auto-generated method stub
        return null;
    }

}
