package com.vcvinci.protocol.util;

import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.protocol.request.ApiVersionRequest;
import com.vcvinci.protocol.request.CommitOffsetRequest;
import com.vcvinci.protocol.request.CreateTopicsRequest;
import com.vcvinci.protocol.request.DeleteGroupsRequest;
import com.vcvinci.protocol.request.FetchOffsetRequest;
import com.vcvinci.protocol.request.FetchRequest;
import com.vcvinci.protocol.request.FindCoordinatorRequest;
import com.vcvinci.protocol.request.GetGroupInfoRequest;
import com.vcvinci.protocol.request.GetGroupMembersInfoRequest;
import com.vcvinci.protocol.request.GetOffsetByTimeRequest;
import com.vcvinci.protocol.request.HeartbeatRequest;
import com.vcvinci.protocol.request.JoinGroupRequest;
import com.vcvinci.protocol.request.LeaderAndSrRequest;
import com.vcvinci.protocol.request.LeaveGroupRequest;
import com.vcvinci.protocol.request.MetadataRequest;
import com.vcvinci.protocol.request.OffsetForLeaderEpochRequest;
import com.vcvinci.protocol.request.PartitionsOffsetRequest;
import com.vcvinci.protocol.request.SendRequest;
import com.vcvinci.protocol.request.StopReplicaRequest;
import com.vcvinci.protocol.request.SyncGroupRequest;
import com.vcvinci.protocol.request.UpdateMetadataRequest;
import com.vcvinci.protocol.response.ApiVersionResponse;
import com.vcvinci.protocol.response.CommitOffsetResponse;
import com.vcvinci.protocol.response.CreateTopicsResponse;
import com.vcvinci.protocol.response.DeleteGroupsResponse;
import com.vcvinci.protocol.response.DoveExceptionResponse;
import com.vcvinci.protocol.response.FetchOffsetResponse;
import com.vcvinci.protocol.response.FetchResponse;
import com.vcvinci.protocol.response.FindCoordinatorResponse;
import com.vcvinci.protocol.response.GetGroupInfoResponse;
import com.vcvinci.protocol.response.GetGroupMembersInfoResponse;
import com.vcvinci.protocol.response.GetOffsetByTimeResponse;
import com.vcvinci.protocol.response.HeartbeatResponse;
import com.vcvinci.protocol.response.JoinGroupResponse;
import com.vcvinci.protocol.response.LeaderAndSrResponse;
import com.vcvinci.protocol.response.LeaveGroupResponse;
import com.vcvinci.protocol.response.MetadataResponse;
import com.vcvinci.protocol.response.OffsetForLeaderEpochResponse;
import com.vcvinci.protocol.response.PartitionsOffsetResponse;
import com.vcvinci.protocol.response.SendResponse;
import com.vcvinci.protocol.response.StopReplicaResponse;
import com.vcvinci.protocol.response.SyncGroupResponse;
import com.vcvinci.protocol.response.UpdateMetadataResponse;

import java.nio.ByteBuffer;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月9日 下午2:12:14
 * @description 类说明
 */
public enum RequestResponseMapper {

    /**
     * 功能描述: 获取Broker所支持的所有请求版本
     */
    ApiVersion((short) 0,
            "ApiVersionRequest",
            ApiVersionRequest.schemaVersions(),
            ApiVersionResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),
    /**
     * 功能描述: 生产者发送消息
     */
    Send((short) 1,
            "SendRequest",
            SendRequest.schemaVersions(),
            SendResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),
    /**
     * 组协调器： 加入组请求 类型
     * 处理器类: com.ucarinc.dove.server.processor.JoinGroupRequestProcessor
     */
    JoinGroup((short) 2,
            "JoinGroupRequest",
            JoinGroupRequest.schemaVersions(),
            JoinGroupResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),

    /**
     * 组协调器： 同步组请求 类型
     * 处理器类: com.ucarinc.dove.server.processor.SyncGroupRequestProcessor
     */
    SyncGroup((short) 3,
            "SyncGroupRequest",
            SyncGroupRequest.schemaVersions(),
            SyncGroupResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),

    /**
     * 功能描述: 离开组
     */
    LeaveGroup((short) 4,
            "LevelGroupRequest",
            LeaveGroupRequest.schemaVersions(),
            LeaveGroupResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),

    GetGroupInfo((short) 5,
            "GetGroupInfoRequest",
            GetGroupInfoRequest.schemaVersions(),
            GetGroupInfoResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),

    /**
     * 功能描述: 返回 group 中各个 member 的详细信息
     */
    GetGroupMembersInfo((short) 6,
            "GetGroupMembersInfoRequest",
            GetGroupMembersInfoRequest.schemaVersions(),
            GetGroupMembersInfoResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),

    /**
     * 功能描述: 提交offset
     */
    CommitOffset((short) 7,
            "CommitOffsetRequest",
            CommitOffsetRequest.schemaVersions(),
            CommitOffsetResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),

    /**
     * 功能描述: 拉取offset
     */
    FetchOffset((short) 8,
            "FetchOffsetRequest",
            FetchOffsetRequest.schemaVersions(),
            FetchOffsetResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),
    /**
     * 功能描述: 通过指定时间获取offset ->消息回溯
     */
    GetOffsetByTime((short) 9,
            "GetOffsetByTimeRequest",
            GetOffsetByTimeRequest.schemaVersions(),
            GetOffsetByTimeResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),

    /**
     * 功能描述: 心跳
     */
    Heartbeat((short) 10,
            "HeartbeatRequest",
            HeartbeatRequest.schemaVersions(),
            HeartbeatResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),

    /**
     * 功能描述: 查找协调组
     */
    FindCoordinator((short) 11,
            "FindCoordinatorRequest",
            FindCoordinatorRequest.schemaVersions(),
            FindCoordinatorResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),

    /**
     * 功能描述: 删除组
     */
    DeleteGroups((short) 12,
            "DeleteGroupsRequest",
            DeleteGroupsRequest.schemaVersions(),
            DeleteGroupsResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),

    /**
     * 功能描述: 获取元数据信息
     */
    Metadata((short) 13,
            "MetadataRequest",
            MetadataRequest.schemaVersions(),
            MetadataResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),

    /**
     * 功能描述: 更新元数据信息
     */
    UpdateMetadata((short) 14,
            "UpdateMetadataRequest",
            UpdateMetadataRequest.schemaVersions(),
            UpdateMetadataResponse.schemaVersions(),
            RemoteTypeConstant.CONTROLLER_TO_SERVER),

    /**
     * 功能描述: 同步副本 broker之间通信
     */
    LeaderAndSr((short) 15,
            "LeaderAndSr",
            LeaderAndSrRequest.schemaVersions(),
            LeaderAndSrResponse.schemaVersions(),
            RemoteTypeConstant.CONTROLLER_TO_SERVER),

    StopReplica((short) 16,
            "StopReplica",
            StopReplicaRequest.schemaVersions(),
            StopReplicaResponse.schemaVersions(),
            RemoteTypeConstant.CONTROLLER_TO_SERVER),

    Pull((short) 17,
            "PullRequest",
            FetchRequest.schemaVersions(),
            FetchResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),

    /** 创建TOPIC请求 **/
    CreateTopics((short) 18,
            "CreateTopicsRequest",
            CreateTopicsRequest.schemaVersions(),
            CreateTopicsResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),

    /** 创建TOPIC请求 **/
    PartitionsOffset((short) 19,
            "PartitionsOffset",
            PartitionsOffsetRequest.schemaVersions(),
            PartitionsOffsetResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),

    OffsetsForLeaderEpoch((short) 20,
            "OffsetsForLeaderEpoch",
            OffsetForLeaderEpochRequest.schemaVersions(),
            OffsetForLeaderEpochResponse.schemaVersions(),
            RemoteTypeConstant.SERVER_TO_SERVER),

    ExceptionResponse((short) 21,
            "ExceptionResponse",
            DoveExceptionResponse.schemaVersions(),
            DoveExceptionResponse.schemaVersions(),
            RemoteTypeConstant.SERVER_TO_CLIENT),
    /*InitProducerId((short) 16,
            "InitProducerIdRequest",
            FetchRequest.schemaVersions(),
            FetchResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),

    Produce((short) 17,
            "ProduceRequest",
            FetchRequest.schemaVersions(),
            FetchResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),




    DeleteRecords((short) 20,
            "DeleteRecordsRequest",
            FetchRequest.schemaVersions(),
            FetchResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),

    CreatePartitions((short) 21,
            "CreatePartitionsRequest",
            FetchRequest.schemaVersions(),
            FetchResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),

    DescribeLogDirs((short) 22,
            "DescribeLogDirsRequest",
            FetchRequest.schemaVersions(),
            FetchResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),

    AlterConfigs((short) 23,
            "AlterConfigsRequest",
            FetchRequest.schemaVersions(),
            FetchResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),

    DescribeConfigs((short) 24,
            "DescribeConfigsRequest",
            FetchRequest.schemaVersions(),
            FetchResponse.schemaVersions(),
            RemoteTypeConstant.CLIENT_TO_SERVER),*/;

    private short code;
    private String name;
    private Schema[] requestSchemas;
    private Schema[] responseSchemas;
    private byte requestType;
    private static RequestResponseMapper[] code2Mappers;

    private static final int MIN_API_CODE = 0;
    public static final int MAX_API_CODE;
    private static final RequestResponseMapper[] CODE_TO_TYPE;

    static {
        int maxKey = -1;
        for (RequestResponseMapper key : RequestResponseMapper.values()) {
            maxKey = Math.max(maxKey, key.code);
        }
        RequestResponseMapper[] codeToType = new RequestResponseMapper[maxKey + 1];
        for (RequestResponseMapper key : RequestResponseMapper.values()) {
            codeToType[key.code] = key;
        }
        CODE_TO_TYPE = codeToType;
        MAX_API_CODE = maxKey;
        code2Mappers = codeToType;
    }

    private RequestResponseMapper(short code, String name, Schema[] requestSchemas, Schema[] responseSchemas,
                                  byte requestType) {
        this.code = code;
        this.name = name;
        this.requestSchemas = requestSchemas;
        this.responseSchemas = responseSchemas;
        this.requestType = requestType;
    }

    public static RequestResponseMapper getMapper(short code) {
        return code2Mappers[code];
    }

    public short getCode() {
        return this.code;
    }

    public short minVersion() {
        return 0;
    }

    public short maxVersion() {
        return (short) (requestSchemas.length - 1);
    }

    private Schema schemaFor(Schema[] schemas, short version) {
        if (!isSupportVersion(version)) {
            throw new RuntimeException("not support the version,name:" + name + ",version:" + version);
        }
        return schemas[version];
    }

    public boolean isSupportVersion(short version) {
        return version >= minVersion() && version <= maxVersion();
    }

    public Schema getRequestSchema(short version) {
        return schemaFor(this.requestSchemas, version);
    }

    public Schema getResponseSchema(short version) {
        return schemaFor(this.responseSchemas, version);
    }

    public Struct parseRequest(short version, ByteBuffer buffer) {
        return getRequestSchema(version).read(buffer);
    }

    public Struct parseResponse(short version, ByteBuffer buffer) {
        return getResponseSchema(version).read(buffer);
    }

    public static RequestResponseMapper forCode(int code) {
        if (!hasCode(code)) {
            throw new IllegalArgumentException(String.format("Unexpected RequestResponseMapper code `%s`, it should be between `%s` " +
                    "and `%s` (inclusive)", code, MIN_API_CODE, MAX_API_CODE));
        }
        return CODE_TO_TYPE[code];
    }

    public static boolean hasCode(int code) {
        return code >= MIN_API_CODE && code <= MAX_API_CODE;
    }

    public int getRequestType() {
        return this.requestType;
    }

    public static final class RemoteTypeConstant {

        public static final byte CLIENT_TO_CLIENT = (byte) 1;
        public static final byte CLIENT_TO_SERVER = (byte) 2;
        public static final byte CLIENT_TO_CONTROLLER = (byte) 4;
        public static final byte SERVER_TO_CLIENT = (byte) 8;
        public static final byte SERVER_TO_CONTROLLER = (byte) 16;
        public static final byte CONTROLLER_TO_CLIENT = (byte) 32;
        public static final byte CONTROLLER_TO_SERVER = (byte) 64;
        public static final byte SERVER_TO_SERVER = (byte) 128;
    }
}
