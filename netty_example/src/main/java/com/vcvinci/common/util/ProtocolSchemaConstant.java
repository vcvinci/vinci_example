package com.vcvinci.common.util;

/**
 * @author yupeng.sun@ucarinc.com
 * @version 1.0 创建时间：2017-12-14 下午3:08:51
 * @Description:
 */
public class ProtocolSchemaConstant {

    // api version response
    public static final String API_VERSION_KEY_NAME = "api_version";
    public static final String API_KEY_NAME = "api_key";
    public static final String MIN_VERSION_KEY_NAME = "min_version";
    public static final String MAX_VERSION_KEY_NAME = "max_version";

    // metadata response
    public static final String BROKERS_KEY_NAME = "brokers";
    public static final String TOPIC_METADATA_KEY_NAME = "topic_metadata";
    public static final String BROKER_ID_KEY_NAME = "broker_id";
    public static final String HOST_KEY_NAME = "host";
    public static final String PORT_KEY_NAME = "port";
    public static final String CONTROLLER_ID_KEY_NAME = "controller_id";
    public static final int NO_CONTROLLER_ID = -1;
    public static final String CLUSTER_ID_KEY_NAME = "cluster_id";
    public static final String IS_INTERNAL_KEY_NAME = "is_internal";
    public static final String PARTITION_METADATA_KEY_NAME = "partition_metadata";
    public static final String LEADER_KEY_NAME = "leader";
    public static final String REPLICAS_KEY_NAME = "replicas";
    public static final String REPLICA_ID_KEY_NAME = "replica_id";
    public static final String SR_KEY_NAME = "sr";
    public static final String OFFLINE_REPLICAS_KEY_NAME = "offline_replicas";

    public static final String CONFIG_ENTRIES_KEY_NAME = "config_entries";
    public static final String CONFIG_NAME_KEY_NAME = "config_name";
    public static final String CONFIG_VALUE_KEY_NAME = "config_value";
    public static final String VALIDATE_ONLY_KEY_NAME = "validate_only";
    public static final String TOPICS_KEY_NAME = "topics";
    public static final String ALLOW_AUTO_TOPIC_CREATION_KEY_NAME = "allow_auto_topic_creation";
    public static final String ACKS_KEY_NAME = "acks";
    public static final String TIMEOUT_KEY_NAME = "timeout";
    public static final String REQUEST_TIME_KEY_NAME = "request_time";
    public static final String TOPIC_KEY_NAME = "topic";
    public static final String TOPIC_ERRORS_KEY_NAME = "topic_errors";
    public static final String GROUP_LIST_KEY = "group_list";
    public static final String GROUP_KEY_NAME = "groupName";
    public static final String MEMBER_ID_KEY_NAME = "memberId";
    public static final String CLIENT_HOST_KEY = "client_host";
    public static final String PARTITION_KEY_NAME = "partition";
    public static final String PARTITIONS_KEY_NAME = "partitions";
    public static final String NUM_PARTITIONS = "num_partitions";
    public static final String REPLICATION_FACTOR_KEY_NAME = "replication_factor";
    public static final String REPLICA_ASSIGNMENT_KEY_NAME = "replica_assignment";
    public static final String OFFSET_KEY_NAME = "offset";
    public static final String RESPONSES_KEY_NAME = "partition_responses";
    public static final String RECORD = "record";
    public static final String RECORD_BATCH = "record_batch";
    public static final String RECORDS = "records";
    public static final String RECORDS_COUNT_KEY_NAME = "records_count";
    public static final String CODE_KEY_NAME = "code";
    public static final String ERROR_CODE_KEY_NAME = "error_code";
    public static final String ERROR_MESSAGE_KEY_NAME = "error_message";
    public static final String ID_KEY_NAME = "id";
    public static final String STATAS_CODE_NAME = "status_code";
    public static final String STORE_TIME_NAME = "store_time";
    public static final String DELAY_TIME_NAME = "delay_time";
    public static final String MAX_SIZE_NAME = "max_size";
    public static final String MAX_OFFSET_NAME = "max_offset";
    public static final String EARLIEST_OFFSET_NAME = "earliest_offset";
    public static final String DATA_LENGTH_NAME = "data_length";
    public static final String DATA_NAME = "data";
    public static final String RESPONSE_TIME_NAME = "response_time";
    public static final String FETCH_LEVEL_KEY_NAME = "fetch_level";
    public static final String TIMESTAMP_KEY_NAME = "timestamp";
    public static final String COORDINATOR_KEY_NAME = "coordinater";
    public static final String METADATA_KEY_NAME = "metadata";
    public static final String GENERATION_ID_KEY_NAME = "generation_id";
    public static final String CONTROLLER_EPOCH_KEY_NAME = "controller_epoch";
    public static final String PARTITION_STATES_KEY_NAME = "partition_states";
    public static final String LEADER_EPOCH = "leader_epoch";
    public static final String LIVE_BROKERS_KEY_NAME = "live_brokers";
    public static final String SESSION_TIMEOUT_KEY_NAME = "session_timeout";
    public static final String REBALANCE_TIMEOUT_KEY_NAME = "rebalance_timeout";
    public static final String GROUP_STATE_KEY_NAME = "group_states";
    public static final String GROUP_PROTOCOLS_KEY_NAME = "group_protocols";
    public static final String SUPPORTED_PARTITION_ASSIGNOR = "supported_partition_assignor";
    public static final String MEMBER_ASSIGNMENT_KEY_NAME = "member_assignment";
    public static final String GROUP_ASSIGNMENT_KEY_NAME = "group_assignment";
    public static final String TOPIC_PARTITIONS_KEY_NAME = "topic_partitions";
    public static final String MAX_WAIT_TIME_MS_KEY_NAME = "max_wait_time_ms";
    public static final String MIN_BYTES_KEY_NAME = "min_byte";
    public static final String MAX_BYTES_KEY_NAME = "max_byte";
    public static final String FETCH_OFFSET_KEY_NAME = "fetch_offset";
    public static final String LOG_START_OFFSET_KEY_NAME = "log_start_offset";
    public static final String LENGTH_KEY_NAME = "length";
    public static final String MAGIC_KEY_NAME = "magic";
    public static final String PARTITION_LEADER_EPOCH_KEY_NAME = "partition_leader_epoch";
    public static final String CRC_KEY_NAME = "crc";
    public static final String ATTRIBUTES_KEY_NAME = "attributes";
    public static final String LAST_OFFSET_DELTA_KEY_NAME = "last_offset_delta";
    public static final String HIGH_WATERMARK_KEY_NAME = "high_watermark";
    public static final String FIRST_CLIENT_TIMESTAMP_KEY_NAME = "first_client_timestamp";
    public static final String LAST_CLIENT_TIMESTAMP_KEY_NAME = "last_client_timestamp";
    public static final String STORE_TIMESTAMP_KEY_NAME = "store_timestamp";
    public static final String PRODUCER_ID_KEY_NAME = "producer_id";
    public static final String PRODUCER_EPOCH_KEY_NAME = "producer_epoch";
    public static final String BASE_SEQUENCE_KEY_NAME = "base_sequence";
    public static final String VERSION_KEY_NAME = "version";
    public static final String METADATA_VERSION_KEY_NAME = "metadata_version";
    public static final String FLAG_KEY_NAME = "flag";
    public static final String CLIENT_ID_KEY_NAME = "client_id";
    public static final String CLIENT_TIME_KEY_NAME = "client_time";
    public static final String STORE_TIME_KEY_NAME = "store_time";
    public static final String MESSAGE_ID_KEY_NAME = "message_id";
    public static final String KEY_KEY_NAME = "key";
    public static final String KEY_KEY_SIZE_NAME = "key_size";
    public static final String VALUE_KEY_NAME = "value";
    public static final String VALUE_KEY_SIZE_NAME = "value_size";
    public static final String GROUP_MEMBERS = "group_members";
    public static final String PARTITION_DATA = "partition_data";
    public static final String RESPONSE_DATA = "response_data";
    public static final String CONSUME_TIME = "consume_time";
    public static final String COMMIT_TIME = "commit_time";
    public static final String COMMIT_TIMESTAMP = "commit_timestamp";
    public static final String CURRENT_STATE_TIMESTAMP_KEY = "current_state_timestamp";
    public static final String COMMON_SERVER_HEADER_KEY_NAME = "common_server_header";
    public static final String COMMON_CLIENT_HEADER_KEY_NAME = "common_client_header";
    public static final String CLIENT_SIMPLE_RECORD_HEADER_KEY_NAME = "client_simple_record";
    public static final String SERVER_SIMPLE_RECORD_HEADER_KEY_NAME = "server_simple_record";
    public static final String CLIENT_BATCH_RECORD_HEADER_KEY_NAME = "client_batch_record";
    public static final String SERVER_BATCH_RECORD_HEADER_KEY_NAME = "server_batch_record";
    public static final String IS_NEW_KEY_NAME = "is_new";

    public static final String TOPIC_NAME_KEY_NAME = "topic";
    public static final String PARTITION_ID_KEY_NAME = "partition";
    public static final String DELETE_PARTITIONS_KEY_NAME = "delete_partitions";

    public static final String PARTITION_EPOCH_OFFSET_KEY_NAME = "partition_epoch_offset";

    public static final String LOG_END_OFFSET_KEY_NAME = "log_end_offset";

    public static final String FOLLOWER_LOG_START_OFFSET_KEY_NAME = "follower_log_start_offset";

    public static final String FETCH_TIMESTAMP_KEY_NAME = "fetch_timestamp";
}
