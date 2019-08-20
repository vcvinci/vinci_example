package com.vcvinci.protocol.request;

import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.protocol.response.AbstractResponse;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月9日 下午2:42:19
 * @description 类说明
 */
public class LeaveGroupRequest extends AbstractRequest {

    private static final Schema LEAVE_GROUP_REQUEST_V0 = new Schema(CommonField.GROUP_NAME, CommonField.MEMBER_ID);
    private String groupName;
    private String memberId;

    public LeaveGroupRequest(String groupName, String memberId, short version) {
        super(RequestResponseMapper.LeaveGroup, version);
        this.groupName = groupName;
        this.memberId = memberId;
    }

    public LeaveGroupRequest(Struct struct, short version) {
        super(RequestResponseMapper.LeaveGroup, version);
        groupName = struct.get(CommonField.GROUP_NAME);
        memberId = struct.get(CommonField.MEMBER_ID);
    }

    public static Schema[] schemaVersions() {
        return new Schema[]{LEAVE_GROUP_REQUEST_V0};
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.LeaveGroup.getRequestSchema(getVersion()));
        struct.set(CommonField.GROUP_NAME, groupName);
        struct.set(CommonField.MEMBER_ID, memberId);
        return struct;
    }

    @Override
    public AbstractResponse getErrorResponse(int throttleTime, Throwable e) {
        // TODO Auto-generated method stub
        return null;
    }

    public static LeaveGroupRequest parse(ByteBuffer buffer, short version) {
        return new LeaveGroupRequest(RequestResponseMapper.LeaveGroup.parseRequest(version, buffer), version);
    }

    public String groupName() {
        return this.groupName;
    }

    public String memberId() {
        return this.memberId;
    }
}
