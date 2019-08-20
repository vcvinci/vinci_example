package com.vcvinci.protocol.response;

import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月5日 上午10:55:01
 * @description 类说明
 */
public class ApiVersionResponse extends AbstractResponse {

    private static final Schema API_VERSIONS_V0 = new Schema(
            new Field(ProtocolSchemaConstant.API_KEY_NAME, CommonTypes.INT16, "API key."),
            new Field(ProtocolSchemaConstant.MIN_VERSION_KEY_NAME, CommonTypes.INT16, "Minimum supported version."),
            new Field(ProtocolSchemaConstant.MAX_VERSION_KEY_NAME, CommonTypes.INT16, "Maximum supported version."));

    private static final Schema API_VERSIONS_RESPONSE_V0 = new Schema(
            CommonField.ERROR_CODE,
            new Field(ProtocolSchemaConstant.API_VERSION_KEY_NAME, new CommonTypes.ARRAY(API_VERSIONS_V0), "API versions supported by the broker."));

    public static Schema[] schemaVersions() {
        return new Schema[]{API_VERSIONS_RESPONSE_V0};
    }

    private final ErrorCodes error;
    private final Map<Short, ApiVersion> apiCodeToApiVersion;

    public ApiVersionResponse(short version, ErrorCodes error, List<ApiVersion> apiVersions) {
        super(version);
        this.error = error;
        this.apiCodeToApiVersion = buildApiCodeToApiVersion(apiVersions);
    }

    public ApiVersionResponse(Struct struct, short version) {
        super(version);
        this.error = ErrorCodes.forCode(struct.get(CommonField.ERROR_CODE));
        List<ApiVersion> tempApiVersions = new ArrayList<>();
        for (Object apiVersionsObj : struct.getArray(ProtocolSchemaConstant.API_VERSION_KEY_NAME)) {
            Struct apiVersionStruct = (Struct) apiVersionsObj;
            short apiKey = apiVersionStruct.getShort(ProtocolSchemaConstant.API_KEY_NAME);
            short minVersion = apiVersionStruct.getShort(ProtocolSchemaConstant.MIN_VERSION_KEY_NAME);
            short maxVersion = apiVersionStruct.getShort(ProtocolSchemaConstant.MAX_VERSION_KEY_NAME);
            tempApiVersions.add(new ApiVersion(apiKey, minVersion, maxVersion));
        }
        this.apiCodeToApiVersion = buildApiCodeToApiVersion(tempApiVersions);
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.ApiVersion.getResponseSchema(getVersion()));
        struct.set(CommonField.ERROR_CODE, error.code());
        List<Struct> apiVersionList = new ArrayList<>();
        for (ApiVersion apiVersion : apiCodeToApiVersion.values()) {
            Struct apiVersionStruct = struct.instance(ProtocolSchemaConstant.API_VERSION_KEY_NAME);
            apiVersionStruct.set(ProtocolSchemaConstant.API_KEY_NAME, apiVersion.apiCode);
            apiVersionStruct.set(ProtocolSchemaConstant.MIN_VERSION_KEY_NAME, apiVersion.minVersion);
            apiVersionStruct.set(ProtocolSchemaConstant.MAX_VERSION_KEY_NAME, apiVersion.maxVersion);
            apiVersionList.add(apiVersionStruct);
        }
        struct.set(ProtocolSchemaConstant.API_VERSION_KEY_NAME, apiVersionList.toArray());
        return struct;
    }

    @Override
    public ErrorCodes getError() {
        return error;
    }

    public Map<Short, ApiVersion> getApiCodeToApiVersion() {
        return apiCodeToApiVersion;
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection<ApiVersion> getApiVersions() {
        return apiCodeToApiVersion.values();
    }

    private Map<Short, ApiVersion> buildApiCodeToApiVersion(List<ApiVersion> apiVersions) {
        Map<Short, ApiVersion> tempApiCodeToApiVersion = new HashMap<>(64);
        for (ApiVersion apiVersion : apiVersions) {
            tempApiCodeToApiVersion.put(apiVersion.apiCode, apiVersion);
        }
        return tempApiCodeToApiVersion;
    }

    public static final class ApiVersion {

        public final short apiCode;
        public final short minVersion;
        public final short maxVersion;

        public ApiVersion(RequestResponseMapper apiCode) {
            this(apiCode.getCode(), apiCode.minVersion(), apiCode.maxVersion());
        }

        public ApiVersion(short apiCode, short minVersion, short maxVersion) {
            this.apiCode = apiCode;
            this.minVersion = minVersion;
            this.maxVersion = maxVersion;
        }

        @Override
        public String toString() {
            return "ApiVersion(" +
                    "apiCode=" + apiCode +
                    ", minVersion=" + minVersion +
                    ", maxVersion= " + maxVersion +
                    ")";
        }
    }

}