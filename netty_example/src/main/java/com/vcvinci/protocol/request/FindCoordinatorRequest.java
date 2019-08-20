package com.vcvinci.protocol.request;

import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.protocol.response.AbstractResponse;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 上午8:14:39
 * @description 查找coordinator的请求类
 */
public class FindCoordinatorRequest extends AbstractRequest {

    private static final Schema FIND_COORDINATOR_REQUEST_V0 = new Schema(CommonField.GROUP_NAME);

    private String groupName;

    public FindCoordinatorRequest(short version, String groupName) {
        super(RequestResponseMapper.FindCoordinator, version);
        this.groupName = groupName;
    }

    public FindCoordinatorRequest(Struct struct, short version) {
        super(RequestResponseMapper.FindCoordinator, version);
        this.groupName = struct.get(CommonField.GROUP_NAME);
    }

    public static Schema[] schemaVersions() {
        return new Schema[]{FIND_COORDINATOR_REQUEST_V0};
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.FindCoordinator.getRequestSchema(getVersion()));
        struct.set(CommonField.GROUP_NAME, this.groupName);
        return struct;
    }

    @Override
    public AbstractResponse getErrorResponse(int throttleTime, Throwable e) {
        // TODO Auto-generated method stub
        return null;
    }

    public static FindCoordinatorRequest parse(ByteBuffer buffer, short version) {
        return new FindCoordinatorRequest(RequestResponseMapper.FindCoordinator.parseRequest(version, buffer), version);
    }

    public String groupName() {
        return this.groupName;
    }

}
 