package com.vcvinci.protocol.util;

import com.vcvinci.common.config.util.IdGenerator;
import com.vcvinci.common.exception.client.NonsupportReqRespMapperException;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.protocol.network.NetworkSend;
import com.vcvinci.protocol.request.AbstractRequest;
import com.vcvinci.protocol.request.ApiVersionRequest;
import com.vcvinci.protocol.request.CommitOffsetRequest;
import com.vcvinci.protocol.request.DeleteGroupsRequest;
import com.vcvinci.protocol.request.FetchOffsetRequest;
import com.vcvinci.protocol.request.FetchRequest;
import com.vcvinci.protocol.request.FindCoordinatorRequest;
import com.vcvinci.protocol.request.GetGroupInfoRequest;
import com.vcvinci.protocol.request.GetGroupMembersInfoRequest;
import com.vcvinci.protocol.request.Header;
import com.vcvinci.protocol.request.HeartbeatRequest;
import com.vcvinci.protocol.request.JoinGroupRequest;
import com.vcvinci.protocol.request.LeaderAndSrRequest;
import com.vcvinci.protocol.request.LeaveGroupRequest;
import com.vcvinci.protocol.request.MetadataRequest;
import com.vcvinci.protocol.request.OffsetForLeaderEpochRequest;
import com.vcvinci.protocol.request.Request;
import com.vcvinci.protocol.request.RequestHeader;
import com.vcvinci.protocol.request.RequestResponse;
import com.vcvinci.protocol.request.SendRequest;
import com.vcvinci.protocol.request.StopReplicaRequest;
import com.vcvinci.protocol.request.SyncGroupRequest;
import com.vcvinci.protocol.request.UpdateMetadataRequest;
import com.vcvinci.protocol.response.AbstractResponse;
import com.vcvinci.protocol.response.ApiVersionResponse;
import com.vcvinci.protocol.response.CommitOffsetResponse;
import com.vcvinci.protocol.response.DeleteGroupsResponse;
import com.vcvinci.protocol.response.FetchOffsetResponse;
import com.vcvinci.protocol.response.FetchResponse;
import com.vcvinci.protocol.response.FindCoordinatorResponse;
import com.vcvinci.protocol.response.GetGroupInfoResponse;
import com.vcvinci.protocol.response.GetGroupMembersInfoResponse;
import com.vcvinci.protocol.response.HeartbeatResponse;
import com.vcvinci.protocol.response.JoinGroupResponse;
import com.vcvinci.protocol.response.LeaderAndSrResponse;
import com.vcvinci.protocol.response.LeaveGroupResponse;
import com.vcvinci.protocol.response.MetadataResponse;
import com.vcvinci.protocol.response.OffsetForLeaderEpochResponse;
import com.vcvinci.protocol.response.ResponseHeader;
import com.vcvinci.protocol.response.SendResponse;
import com.vcvinci.protocol.response.StopReplicaResponse;
import com.vcvinci.protocol.response.SyncGroupResponse;
import com.vcvinci.protocol.response.UpdateMetadataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class ProcotolHelper {

    public static final Logger LOGGER = LoggerFactory.getLogger(ProcotolHelper.class);

    public static RequestResponse parseBody(boolean isResponse, RequestHeader header, ByteBuffer buf) {
        if (isResponse) {
            return parseResponseBody(RequestResponseMapper.getMapper(header.getCode()), header.getApiVersion(), buf);
        }
        return parseRequestBody(RequestResponseMapper.getMapper(header.getCode()), header.getApiVersion(), buf);
    }

    // public static RequestResponse decode(ByteBuffer buf) {
    // byte flag = buf.get();
    // short code = buf.getShort();
    // short version = buf.getShort();
    // buf.rewind();
    // boolean isRequest = isResponse(flag);
    // if (isRequest) {
    // return parseRequest(RequestResponseMapper.getMapper(code), version, buf);
    // }
    //
    // return
    // }

    public static ResponseHeader parseToResponseHeader(RequestHeader header) {
        return new ResponseHeader(header.getApiVersion(), header.getId());
    }

    public static ResponseHeader parseToResponseHeader(ByteBuffer buf) {
        Struct struct = ResponseHeader.SCHEMA.read(buf);
        return new ResponseHeader(struct);
    }

    public static RequestHeader parseToRequestHeader(ByteBuffer buf) {
        Struct struct = RequestHeader.SCHEMA.read(buf);
        return new RequestHeader(struct);
    }

    public static NetworkSend buildNetworkSend(String destination, Request body) {
        return buildNetworkSend(destination, body, body.getCode(), IdGenerator.generate());
    }

    public static NetworkSend buildNetworkSend(String destination, RequestResponse body, short code, int id) {
        final Header header = new RequestHeader(code, body.getVersion(), id);
        return new NetworkSend(destination, header, body);
    }

    public static NetworkSend buildNetworkSendOfServer(String destination, RequestResponse body, short code, int id) {
        final Header header = new ResponseHeader(body.getVersion(), id);
        return new NetworkSend(destination, header, body);
    }

    /**
     * for test
     */
    public static NetworkSend buildNetworkReviced(String destination, RequestResponse body, int id) {
        final Header header = new ResponseHeader(body.getVersion(), id);
        return new NetworkSend(destination, header, body);
    }

    private static AbstractRequest parseRequestBody(RequestResponseMapper mapper, short version, ByteBuffer buf) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("收到请求协议为: {}", mapper.name());
        }
        Struct struct = mapper.parseRequest(version, buf);
        switch (mapper) {
            case ApiVersion:
                return new ApiVersionRequest(version);
            case Send:
                return new SendRequest(struct, version);
            case JoinGroup:
                return new JoinGroupRequest(struct, version);
            case SyncGroup:
                return new SyncGroupRequest(struct, version);
            case LeaveGroup:
                return new LeaveGroupRequest(struct, version);
            case GetGroupInfo:
                return new GetGroupInfoRequest(struct, version);
            case GetGroupMembersInfo:
                return new GetGroupMembersInfoRequest(struct, version);
            case CommitOffset:
                return new CommitOffsetRequest(struct, version);
            case FetchOffset:
                return new FetchOffsetRequest(struct, version);
            case Heartbeat:
                return new HeartbeatRequest(struct, version);
            case FindCoordinator:
                return new FindCoordinatorRequest(struct, version);
            case DeleteGroups:
                return new DeleteGroupsRequest(struct, version);
            case UpdateMetadata:
                return new UpdateMetadataRequest(struct, version);
            case LeaderAndSr:
                return new LeaderAndSrRequest(struct, version);
            case StopReplica:
                return new StopReplicaRequest(struct, version);
            case Metadata:
                return new MetadataRequest(struct, version);
            case Pull:
                return new FetchRequest(struct, version);
            case OffsetsForLeaderEpoch:
                return new OffsetForLeaderEpochRequest(struct, version);
            default:
                throw new NonsupportReqRespMapperException("not support this request:" + mapper);
        }
    }

    public static AbstractResponse parseResponseBody(RequestResponseMapper mapper, short version, ByteBuffer buf) {
        Struct struct = mapper.parseResponse(version, buf);
        switch (mapper) {
            case ApiVersion:
                return new ApiVersionResponse(struct, version);
            case Send:
                return new SendResponse(struct, version);
            case JoinGroup:
                return new JoinGroupResponse(struct, version);
            case SyncGroup:
                return new SyncGroupResponse(struct, version);
            case LeaveGroup:
                return new LeaveGroupResponse(struct, version);
            case GetGroupInfo:
                return new GetGroupInfoResponse(struct, version);
            case GetGroupMembersInfo:
                return new GetGroupMembersInfoResponse(struct, version);
            case CommitOffset:
                return new CommitOffsetResponse(struct, version);
            case FetchOffset:
                return new FetchOffsetResponse(struct, version);
            case Heartbeat:
                return new HeartbeatResponse(struct, version);
            case FindCoordinator:
                return new FindCoordinatorResponse(struct, version);
            case DeleteGroups:
                return new DeleteGroupsResponse(struct, version);
            case UpdateMetadata:
                return new UpdateMetadataResponse(struct, version);
            case Metadata:
                return new MetadataResponse(struct, version);
            case LeaderAndSr:
                return new LeaderAndSrResponse(struct, version);
            case StopReplica:
                return new StopReplicaResponse(struct, version);
            case Pull:
                return new FetchResponse(struct, version);
            case OffsetsForLeaderEpoch:
                return new OffsetForLeaderEpochResponse(struct, version);
            default:
                throw new NonsupportReqRespMapperException("not support this response:" + mapper);
        }
    }

    public static boolean isResponse(byte flag) {
        return (flag & 0x01) == 0x01;
    }

}
