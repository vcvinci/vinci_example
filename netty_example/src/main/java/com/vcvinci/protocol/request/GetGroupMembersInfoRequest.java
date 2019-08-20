package com.vcvinci.protocol.request;

import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.protocol.response.Response;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @CreateDate: 2018/11/12 21:35
 * @Author: zhengquan.lin@ucarinc.com
 * @Description: 服务端会对 传递进来的每个 groupName 做有效性判断。如：是否属于当前协调器管理的消费组
 */
public class GetGroupMembersInfoRequest extends AbstractRequest {

    private static final String GROUP_NAME_LIST_KEY = "group_name_list";

    public static final Schema GET_GROUP_MEMBERS_INFO_REQUEST_V0 = new Schema(
            new Field(GROUP_NAME_LIST_KEY, new CommonTypes.ARRAY<>(CommonTypes.STRING), "消费组名字列表, 如果为空，则返回空结果")
    );

    public static Schema[] schemaVersions() {
        return new Schema[]{GET_GROUP_MEMBERS_INFO_REQUEST_V0};
    }

    private List<String> groupNameList;

    public GetGroupMembersInfoRequest(short version, List<String> groupNameList) {
        super(RequestResponseMapper.GetGroupMembersInfo, version);
        this.groupNameList = groupNameList;
    }

    public GetGroupMembersInfoRequest(Struct struct, short version) {
        super(RequestResponseMapper.GetGroupMembersInfo, version);
        groupNameList = new ArrayList<>(Arrays.asList(struct.getStringArray(GROUP_NAME_LIST_KEY)));
    }

    public List<String> getGroupNameList() {
        return groupNameList;
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.GetGroupMembersInfo.getRequestSchema(getVersion()));
        struct.set(GROUP_NAME_LIST_KEY, groupNameList.toArray());
        return struct;
    }

    @Override
    public Response getErrorResponse(int throttleTime, Throwable e) {
        return null;
    }

    public static GetGroupMembersInfoRequest parse(ByteBuffer buffer, short version) {
        return new GetGroupMembersInfoRequest(RequestResponseMapper.GetGroupMembersInfo.parseRequest(version, buffer), version);
    }

}
