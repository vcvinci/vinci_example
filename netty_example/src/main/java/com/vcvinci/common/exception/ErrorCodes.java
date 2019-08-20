package com.vcvinci.common.exception;

import com.vcvinci.common.exception.api.InvalidGroupException;
import com.vcvinci.common.exception.api.InvalidSessionTimeoutException;
import com.vcvinci.common.exception.api.NotLeaderForPartitionException;
import com.vcvinci.common.exception.api.OffsetOutOfRangeException;
import com.vcvinci.common.exception.api.UnknowTopicOrPartitionException;
import com.vcvinci.common.exception.api.UnknownApiException;
import com.vcvinci.common.exception.api.UnmatchResponseException;
import com.vcvinci.common.exception.client.ConnectClosedException;
import com.vcvinci.common.exception.client.LeaderNotAvailableException;
import com.vcvinci.common.exception.client.NoAvailablePartitionException;
import com.vcvinci.common.exception.client.NoAvaliableHostException;
import com.vcvinci.common.exception.client.NonsupportReqRespMapperException;
import com.vcvinci.common.exception.client.StaleMetadataException;
import com.vcvinci.common.exception.client.TimeoutException;
import com.vcvinci.common.exception.client.TopicAuthorizationException;
import com.vcvinci.common.exception.client.UnknownClientException;
import com.vcvinci.common.exception.client.WriteToChannelException;
import com.vcvinci.common.exception.common.CommonTypeException;
import com.vcvinci.common.exception.common.ParamException;
import com.vcvinci.common.exception.common.SystemException;
import com.vcvinci.common.exception.server.ControllerMovedException;
import com.vcvinci.common.exception.server.CoordinatorLoadInProgressException;
import com.vcvinci.common.exception.server.CoordinatorNotAvailiableException;
import com.vcvinci.common.exception.server.CoordinatorNotCorrectException;
import com.vcvinci.common.exception.server.CoordinatorNotReadyException;
import com.vcvinci.common.exception.server.DoveStorageException;
import com.vcvinci.common.exception.server.GroupInRebalanceException;
import com.vcvinci.common.exception.server.GroupNameNotFoundException;
import com.vcvinci.common.exception.server.GroupNotEmptyException;
import com.vcvinci.common.exception.server.IllegalGenerationException;
import com.vcvinci.common.exception.server.InconsistentGroupProtocolException;
import com.vcvinci.common.exception.server.InvalidCommitOffsetException;
import com.vcvinci.common.exception.server.InvalidRequestException;
import com.vcvinci.common.exception.server.LeaderNotAvailiableException;
import com.vcvinci.common.exception.server.LocalAppendException;
import com.vcvinci.common.exception.server.NotControllerException;
import com.vcvinci.common.exception.server.NotCoordinatorException;
import com.vcvinci.common.exception.server.OperationTimeoutException;
import com.vcvinci.common.exception.server.ReplicaNotAvailiableException;
import com.vcvinci.common.exception.server.ServerNoReplicaException;
import com.vcvinci.common.exception.server.UnknownMemberException;
import com.vcvinci.common.exception.server.UnknownServerException;
import com.vcvinci.common.exception.server.UnknownTopicOrPartitionException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vinci
 * @version 创建时间：2018年5月9日 下午3:47:39
 * 异常枚举类
 */
public enum ErrorCodes {
    /**
     * 1-9999 api 异常、10000-19999 server异常、20000-29999 client异常、30000以上是通用异常
     */
    UNKNOWN_ERROR(-1, "unknown", new UnknowException()),
    NONE(0, "", null),
    API_UNKNOWN_ERROR(1, "未知", new UnknownApiException("The server experienced an unexpected error when processing the request")),
    API_UNMATCH_ERROR(2, "响应不匹配", new UnmatchResponseException("response unmatch error")),
    API_NOT_LEADER_FOR_PARTITION_ERROR(3, "rebalance", new NotLeaderForPartitionException("current is not leader for partition,maybe is rebalance")),
    API_UNKNOWN_TOPIC_OR_PARTITION_ERROR(4, "参数错误", new UnknowTopicOrPartitionException("the topic or partition is unknow in current cluster")),
    API_INVALID_GROUP_ERROR(5, "参数错误", new InvalidGroupException("the group  is invalid")),
    API_INVALID_SESSION_TIMEOUT(6, "参数错误", new InvalidSessionTimeoutException("session timeout parameter for the consumer is invalid")),
    API_OFFSET_OUT_OF_RANGE_ERROR(7, "参数错误", new OffsetOutOfRangeException("the offset out of range")),
    API_TOPIC_AUTHORIZATION_FAILED_ERROR(29, "权限", new TopicAuthorizationException("Topic authorization failed.")),
    SERVER_UNKNOWN_ERROR(10001, "配置", new UnknownServerException("occur some unknown exception at server")),
    SERVER_UNKNOWN_TP_ERROR(10002, "系统错误", new UnknownTopicOrPartitionException("topic or partition not exist")),
    LEADER_NOT_AVAILABLE_ERROR(10003, "系统错误", new LeaderNotAvailiableException(("leader is not availiable now, the leadership is being elected"))),
    REPLICA_NOT_AVAILABLE_ERROR(10004, "系统错误", new ReplicaNotAvailiableException(("replica is not availiable for the required topic-partition"))),
    CONTROLLER_MOVED_ERROR(10005, "系统错误", new ControllerMovedException("controller has removed")),
    SERVER_LOCAL_APPEND_ERROR(10006, "系统错误", new LocalAppendException("can't append messages to local")),
    COORDINATOR_NOT_AVAILABLE(10007, "系统错误", new CoordinatorNotAvailiableException("coordinator is not availiable now, for the partition leader is being elected")),
    COORDINATOR_NOT_CORRECT(10008, "系统错误", new CoordinatorNotCorrectException("the coordinator is not correct for the requested group")),
    COORDINATOR_NOT_READY(10009, "系统错误", new CoordinatorNotReadyException("the coordinator is not ready, because the partition for the group is loading")),
    UNKNOWN_MEMBER_ERROR(10010, "系统错误", new UnknownMemberException("the member is unknown by the coordinator, for the group is not exist")),
    INCONSISTENT_GROUP_PROTOCOL(10011, "系统错误", new InconsistentGroupProtocolException("protocols applied by the consumer is not compatible with other members already exists in the group")),
    GROUP_IN_REBALANCE(10012, "系统错误", new GroupInRebalanceException("group is in rebalancing, please rejoin the group")),
    ILLEGAL_GENERATION(10013, "系统错误", new IllegalGenerationException("the member generation is the same with group generation")),
    SERVER_NO_REPLICA_ERROR(10014, "系统错误", new ServerNoReplicaException("replica not found on server")),
    OPERATION_TIMEOUT(10015, "系统错误", new OperationTimeoutException("operation timeout")),
    INVALID_COMMIT_OFFSET(10017, "无效offset", new InvalidCommitOffsetException("出现新提交的offset值比之前已提交的要小")),
    GROUP_NAME_NOT_FOUND(10018, "消费组不存在", new GroupNameNotFoundException("this group is not found")),
    GROUP_NOT_EMPTY(10019, "消费组不为空", new GroupNotEmptyException("The group is not empty")),
    DOVE_STORAGE(100020, "系统错误", new DoveStorageException("Disk error when trying to access log file on the disk.")),
    NOT_CONTROLLER(100021, "系统错误", new NotControllerException("This is not the correct controller for this cluster.")),
    COORDINATOR_LOAD_IN_PROGRESS(100022, "协调器处于正在加载状态", new CoordinatorLoadInProgressException("The coordinator is loading and hence can't process requests.")),
    NOT_COORDINATOR(100023, "无效的协调器", new NotCoordinatorException("This is not the correct coordinator.")),
    CLIENT_UNKNOWN_ERROR(20001, "未知", new UnknownClientException("occur some unknow exception at client")),
    CLIENT_NO_AVAILABLE_PARTITION_ERROR(20002, "元数据错误", new NoAvailablePartitionException("has no available partition")),
    CLIENT_STALE_METADATA_ERROR(20003, "元数据错误", new StaleMetadataException("metadata is stale")),
    CLIENT_LEADER_NOT_AVAILABLE_ERROR(20004, "元数据错误", new LeaderNotAvailableException("has no leader for this partition")),
    CLIENT_WRITE_TO_CHANNEL_ERROR(20005, "写入管道错误", new WriteToChannelException("send request error when write to channel")),
    CLIENT_CONNECT_CLOSED_ERROR(20006, "连接管道被关闭错误", new ConnectClosedException("connect channel closed")),
    CLIENT_NONSUPPORT_REQ_RESP_ERROR(20007, "不支持得请求或响应", new NonsupportReqRespMapperException("nonsupport request or response")),
    CLIENT_TIMEOUT_ERROR(20008, "请求超时", new TimeoutException("request timeout")),
    CLIENT_NO_HAVE_VALID_HOST_ERROR(20009, "没有有效broker地址", new NoAvaliableHostException("No hava broker host addr")),
    INVALID_REQUEST(20011,"无效的请求",new InvalidRequestException("This most likely occurs because of a request being malformed by the client library or the message was sent to an incompatible broker. See the broker logs for more details.")),
    COMMON_TYPE_ERROR(30001, "类型错误", new CommonTypeException("occur some type error")),
    COMMON_SYSTEM_ERROR(30004, "系统错误", new SystemException("occur some system error")),
    COMMON_PARAM_ERROR(30005, "参数错误", new ParamException("param error")), // todo vinci direct throw runtime
    ;

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorCodes.class);

    private static Map<Class<?>, ErrorCodes> classToError = new HashMap<>();

    private static Map<Short, ErrorCodes> codeToError = new HashMap<>();

    static {
        for (ErrorCodes error : ErrorCodes.values()) {
            if (codeToError.get(error.code()) != null) {
                throw COMMON_SYSTEM_ERROR.buildException("error code duplicate");
            }
            codeToError.put(error.code(), error);
            if (error == NONE) {
                continue;
            }
            if (error.exception == null) {
                throw COMMON_SYSTEM_ERROR.buildException("exception can not be null");
            }
            if (classToError.get(error.exception.getClass()) != null) {
                throw COMMON_SYSTEM_ERROR.buildException("exception class duplicate");
            }
            classToError.put(error.exception.getClass(), error);
        }
    }

    private final DoveException exception;

    private short code;

    private String category;

    ErrorCodes(int code, String category, DoveException exception) {
        this.code = (short) code;
        this.category = category;
        this.exception = exception;
    }

    public boolean instanceOf(Class<? extends Throwable> e) {
        return this.exception != null && this.exception.getClass() == e;
    }

    /**
     * 基于code获取相应error，如果没有指定code则返回UNKNOW_ERROR。
     *
     * @param code
     * @return
     */
    public static ErrorCodes forCode(short code) {
        ErrorCodes error = codeToError.get(code);
        if (error == null) {
            LOGGER.warn("Unexpected error code: {}.", code);
            return UNKNOWN_ERROR;
        }
        return error;
    }

    /**
     * 基于异常类型获取error，如果没有特定异常类型，则返回UNKNOW_ERROR。
     *
     * @param t
     * @return
     */
    public static ErrorCodes forException(Throwable t) {
        if (t == null) {
            return ErrorCodes.NONE;
        }
        Class<?> clazz = t.getClass();
        ErrorCodes error = classToError.get(clazz);
        if (error == null) {
            LOGGER.warn("Unexpected exception class:{}", clazz);
            return UNKNOWN_ERROR;
        }
        return error;
    }

    public short code() {
        return this.code;
    }

    public String getDefaultErrMsg() {
        return this.exception == null ? StringUtils.EMPTY : this.exception.getMessage();
    }

    public DoveException buildException() {
        return this.buildException(null);
    }

    public DoveException buildException(String message) {
        return this.buildException(message, null);
    }

    public DoveException buildException(String message, Throwable cause) {
        if (this == NONE) {
            return null;
        }
        Class<?>[] clazzs = null;
        Object[] params = null;
        if (cause == null) {
            clazzs = new Class<?>[1];
            clazzs[0] = String.class;
            params = new Object[1];
            params[0] = ObjectUtils.defaultIfNull(message, this.exception.getMessage());
        } else {
            clazzs = new Class<?>[2];
            clazzs[0] = String.class;
            clazzs[1] = Throwable.class;
            params = new Object[2];
            params[0] = ObjectUtils.defaultIfNull(message, this.exception.getMessage());
            params[1] = cause;
        }
        try {
            Constructor<?> constructor = this.exception.getClass().getConstructor(clazzs);
            DoveException exception = (DoveException) constructor.newInstance(params);
            return exception;
        } catch (Throwable e) {
            LOGGER.warn("can not build exception,may be has not constructor with String [Throwable] type,class:{}",
                    this.exception.getClass());
        }
        // 使用默认异常
        return exception;
    }
}
