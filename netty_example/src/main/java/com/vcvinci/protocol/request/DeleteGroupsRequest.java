package com.vcvinci.protocol.request;

import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.protocol.response.Response;
import com.vcvinci.protocol.util.RequestResponseMapper;
import org.apache.commons.collections4.CollectionUtils;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @CreateDate: 2018/11/12 21:35
 * @Author: zhengquan.lin@ucarinc.com
 * @Description:
 */
public class DeleteGroupsRequest extends AbstractRequest {

    private static final String GROUP_NAME_LIST_KEY = "group_name_list";

    public static final Schema DELETE_GROUPS_REQUEST_V0 = new Schema(
            new Field(GROUP_NAME_LIST_KEY, new CommonTypes.ARRAY<>(CommonTypes.STRING))
    );

    public static Schema[] schemaVersions() {
        return new Schema[]{DELETE_GROUPS_REQUEST_V0};
    }

    private final Set<String> groupNameSet;

    public DeleteGroupsRequest(short version, Set<String> groupNameSet) {
        super(RequestResponseMapper.DeleteGroups, version);
        if (CollectionUtils.isEmpty(groupNameSet)) {
            throw new IllegalArgumentException("参数必填");
        }
        this.groupNameSet = groupNameSet;
    }

    public DeleteGroupsRequest(Struct struct, short version) {
        super(RequestResponseMapper.DeleteGroups, version);
        String[] datas = struct.getStringArray(GROUP_NAME_LIST_KEY);
        groupNameSet = new HashSet<>(datas.length);
        groupNameSet.addAll(Arrays.asList(datas));
    }

    @Override
    public Response getErrorResponse(int throttleTime, Throwable e) {
        return null;
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.DeleteGroups.getRequestSchema(getVersion()));
        struct.set(GROUP_NAME_LIST_KEY, groupNameSet.toArray());
        return struct;
    }

    public static DeleteGroupsRequest parse(ByteBuffer buffer, short version) {
        return new DeleteGroupsRequest(RequestResponseMapper.DeleteGroups.parseRequest(version, buffer), version);
    }

    public Set<String> getGroupNameSet() {
        return groupNameSet;
    }
}
