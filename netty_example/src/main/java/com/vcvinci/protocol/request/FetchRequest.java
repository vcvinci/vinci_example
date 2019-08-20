package com.vcvinci.protocol.request;

import com.vcvinci.common.partition.TopicPartition;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes.ARRAY;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.response.AbstractResponse;
import com.vcvinci.protocol.util.CollectionUtils;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FetchRequest extends AbstractRequest {

    public static final long INVALID_LOG_START_OFFSET = -1;

    private static final Schema FETCH_REQUEST_PARTITION_V0 = new Schema(
            CommonField.PARTITION_ID,
            CommonField.FETCH_OFFSET,
            CommonField.LOG_START_OFFSET,
            CommonField.MAX_BYTES);

    private static final Schema FETCH_REQUEST_TOPIC_V0 = new Schema(
            CommonField.TOPIC_NAME,
            new Field(ProtocolSchemaConstant.PARTITIONS_KEY_NAME, new ARRAY<>(FETCH_REQUEST_PARTITION_V0),
                    "Partitions to fetch."));

    private static final Schema FETCH_REQUEST_V0 = new Schema(
            CommonField.REPLICA_ID,
            CommonField.MAX_WAIT_TIME_MS,
            CommonField.MIN_BYTES,
            new Field(ProtocolSchemaConstant.TOPICS_KEY_NAME,new ARRAY<>(FETCH_REQUEST_TOPIC_V0), "Topics to fetch in the order provided."));

    public int getReplicaId() {
        return replicaId;
    }

    public int getMaxWaitTimeMs() {
        return maxWaitTimeMs;
    }

    public int getMinBytes() {
        return minBytes;
    }

    private final int replicaId;
    private final int maxWaitTimeMs;
    private final int minBytes;
    private final long requestTimeMs = System.currentTimeMillis();
    private final Map<TopicPartition, OffsetInfo> fetchInfos;

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.Pull.getRequestSchema(getVersion()));
        struct.set(CommonField.REPLICA_ID, replicaId);
        struct.set(CommonField.MAX_WAIT_TIME_MS, maxWaitTimeMs);
        struct.set(CommonField.MIN_BYTES, minBytes);
        Map<String, Map<Integer, OffsetInfo>> topicsData = CollectionUtils.groupDataByTopic(fetchInfos);
        List<Struct> topicArray = new ArrayList<>();
        for (Map.Entry<String, Map<Integer, OffsetInfo>> topicEntry : topicsData.entrySet()) {
            Struct topicData = struct.instance(ProtocolSchemaConstant.TOPICS_KEY_NAME);
            topicData.set(CommonField.TOPIC_NAME, topicEntry.getKey());
            List<Struct> partitionArray = new ArrayList<>();
            for (Map.Entry<Integer, OffsetInfo> partitionEntry : topicEntry.getValue().entrySet()) {
                OffsetInfo offsetInfo = partitionEntry.getValue();
                Struct partitionData = topicData.instance(ProtocolSchemaConstant.PARTITIONS_KEY_NAME);
                partitionData.set(CommonField.PARTITION_ID, partitionEntry.getKey());
                partitionData.set(CommonField.FETCH_OFFSET, offsetInfo.getFetchOffset());
                partitionData.set(CommonField.LOG_START_OFFSET, offsetInfo.getLogStartOffset());
                partitionData.set(CommonField.MAX_BYTES, offsetInfo.getMaxBytes());
                partitionArray.add(partitionData);
            }
            topicData.set(ProtocolSchemaConstant.PARTITIONS_KEY_NAME, partitionArray.toArray());
            topicArray.add(topicData);
        }
        struct.set(ProtocolSchemaConstant.TOPICS_KEY_NAME, topicArray.toArray());
        return struct;

    }

    public FetchRequest(short version, int replicaId, int maxWaitTimeMs, int minBytes,
                        Map<TopicPartition, OffsetInfo> fetchInfos) {
        super(RequestResponseMapper.Pull, version);
        this.replicaId = replicaId;
        this.maxWaitTimeMs = maxWaitTimeMs;
        this.minBytes = minBytes;
        this.fetchInfos = fetchInfos;
    }

    public FetchRequest(Struct struct, short version) {
        super(RequestResponseMapper.Pull, version);
        this.replicaId = struct.get(CommonField.REPLICA_ID);
        this.maxWaitTimeMs = struct.get(CommonField.MAX_WAIT_TIME_MS);
        this.minBytes = struct.get(CommonField.MIN_BYTES);
        fetchInfos = new LinkedHashMap<>();
        for (Struct topicRequest : struct.getStructArray(ProtocolSchemaConstant.TOPICS_KEY_NAME)) {
            String topic = topicRequest.get(CommonField.TOPIC_NAME);
            for (Struct partitionRequest : topicRequest.getStructArray(ProtocolSchemaConstant.PARTITIONS_KEY_NAME)) {
                int partition = partitionRequest.get(CommonField.PARTITION_ID);
                long offset = partitionRequest.get(CommonField.FETCH_OFFSET);
                int maxBytes = partitionRequest.get(CommonField.MAX_BYTES);
                long logStartOffset = partitionRequest.get(CommonField.LOG_START_OFFSET);
                OffsetInfo partitionData = new OffsetInfo(offset, logStartOffset, maxBytes);
                fetchInfos.put(new TopicPartition(topic, partition), partitionData);
            }
        }
    }

    public static Schema[] schemaVersions() {
        return new Schema[]{FETCH_REQUEST_V0};
    }

    @Override
    public AbstractResponse getErrorResponse(int throttleTime, Throwable e) {
        // TODO Auto-generated method stub
        return null;
    }

    public Map<TopicPartition, OffsetInfo> getFetchInfos() {
        return fetchInfos;
    }

    public long timeConsuming() {
        return (System.currentTimeMillis() - requestTimeMs) / 1000;
    }

    public static FetchRequest parse(ByteBuffer buffer, short version) {
        return new FetchRequest(RequestResponseMapper.Pull.parseRequest(version, buffer), version);
    }

    @Override
    public String toString() {
        return "FetchRequest{"
                + "replicaId=" + replicaId
                + ", maxWaitTimeMs=" + maxWaitTimeMs
                + ", minBytes=" + minBytes
                + ", fetchInfos=" + fetchInfos
                + '}';
    }

    public static class OffsetInfo {

        private final long fetchOffset;

        /**
         * 备份副本请求拉取主副本时，带上备份副本自己的最小offset，可能为null
         */
        private final Long logStartOffset;

        private final int maxBytes;

        public OffsetInfo(long fetchOffset, long logStartOffset, int maxBytes) {
            this.fetchOffset = fetchOffset;
            this.logStartOffset = logStartOffset;
            this.maxBytes = maxBytes;
        }

        public long getFetchOffset() {
            return fetchOffset;
        }

        public Long getLogStartOffset() {
            return logStartOffset;
        }

        public int getMaxBytes() {
            return maxBytes;
        }

        @Override
        public String toString() {
            return "OffsetInfo{"
                    + "fetchOffset=" + fetchOffset
                    + ", logStartOffset=" + logStartOffset
                    + ", maxBytes=" + maxBytes
                    + '}';
        }
    }

}
