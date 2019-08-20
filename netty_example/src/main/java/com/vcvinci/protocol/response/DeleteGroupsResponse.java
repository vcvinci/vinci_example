package com.vcvinci.protocol.response;

import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.protocol.response.model.GroupErrorCodeMapping;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @CreateDate: 2018/11/12 21:36
 * @Author: zhengquan.lin@ucarinc.com
 * @Description:
 */
public class DeleteGroupsResponse extends AbstractResponse {

    private static final String GROUP_ERROR_CODES_KEY = "group_error_codes";

    private static final Schema GROUP_ERROR_CODE = new Schema(
            CommonField.GROUP_NAME,
            CommonField.ERROR_CODE);

    private static final Schema DELETE_GROUPS_RESPONSE_V0 = new Schema(
            new Field(GROUP_ERROR_CODES_KEY, new CommonTypes.ARRAY<>(GROUP_ERROR_CODE)));

    public static Schema[] schemaVersions() {
        return new Schema[]{DELETE_GROUPS_RESPONSE_V0};
    }

    private final List<GroupErrorCodeMapping> groupErrorCodeMappingList;

    public DeleteGroupsResponse(short version, List<GroupErrorCodeMapping> groupErrorCodeMappingList) {
        super(version);
        this.groupErrorCodeMappingList = groupErrorCodeMappingList;
    }

    public DeleteGroupsResponse(Struct struct, short version) {
        super(version);
        Object[] datas = struct.getArray(GROUP_ERROR_CODES_KEY);
        groupErrorCodeMappingList = new ArrayList<>(datas.length);
        for (Object object : datas) {
            Struct groupStruct = (Struct) object;
            String groupName = groupStruct.get(CommonField.GROUP_NAME);
            short errorCode = groupStruct.get(CommonField.ERROR_CODE);
            groupErrorCodeMappingList.add(new GroupErrorCodeMapping(groupName, errorCode));
        }
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        return null;
    }

    @Override
    public Struct toStruct() {
        final Struct struct = new Struct(RequestResponseMapper.DeleteGroups.getResponseSchema(getVersion()));
        final List<Struct> groupErrorCodeStructList = new ArrayList<>(groupErrorCodeMappingList.size());
        Struct itemStruct;
        for (GroupErrorCodeMapping groupErrorCodeMapping : groupErrorCodeMappingList) {
            itemStruct = struct.instance(GROUP_ERROR_CODES_KEY);
            itemStruct.set(CommonField.GROUP_NAME, groupErrorCodeMapping.getGroupName());
            itemStruct.set(CommonField.ERROR_CODE, groupErrorCodeMapping.getErrorCode());
            groupErrorCodeStructList.add(itemStruct);
        }
        struct.set(GROUP_ERROR_CODES_KEY, groupErrorCodeStructList.toArray());
        return struct;
    }

    public List<GroupErrorCodeMapping> getGroupErrorCodeMappingList() {
        return groupErrorCodeMappingList;
    }

    public static DeleteGroupsResponse parse(ByteBuffer buffer, short version) {
        return new DeleteGroupsResponse(RequestResponseMapper.DeleteGroups.parseResponse(version, buffer), version);
    }
}
