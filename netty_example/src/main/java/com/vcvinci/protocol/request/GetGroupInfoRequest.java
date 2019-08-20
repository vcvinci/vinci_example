package com.vcvinci.protocol.request;

import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.protocol.response.AbstractResponse;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 上午8:34:42
 * @description 类说明
 */
public class GetGroupInfoRequest extends AbstractRequest {

    /**
     * 是一个空的 schema
     */
    private static final Schema GET_GROUP_INFO_REQUEST_V0 = new Schema();

    public static Schema[] schemaVersions() {
        return new Schema[]{GET_GROUP_INFO_REQUEST_V0};
    }

    public GetGroupInfoRequest(short version) {
        super(RequestResponseMapper.GetGroupInfo, version);
    }

    public GetGroupInfoRequest(Struct struct, short version) {
        super(RequestResponseMapper.GetGroupInfo, version);
    }

    @Override
    public Struct toStruct() {
        return new Struct(RequestResponseMapper.GetGroupInfo.getRequestSchema(getVersion()));
    }

    @Override
    public AbstractResponse getErrorResponse(int throttleTime, Throwable e) {
        // TODO Auto-generated method stub
        return null;
    }

    public static GetGroupInfoRequest parse(ByteBuffer buffer, short version) {
        return new GetGroupInfoRequest(RequestResponseMapper.GetGroupInfo.parseRequest(version, buffer), version);
    }

}
 