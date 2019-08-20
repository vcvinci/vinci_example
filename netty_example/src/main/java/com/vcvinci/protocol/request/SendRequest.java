package com.vcvinci.protocol.request;

import com.vcvinci.common.record.MemoryRecordBatch;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.response.AbstractResponse;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;

import static com.vcvinci.common.schema.CommonTypes.INT16;
import static com.vcvinci.common.schema.CommonTypes.INT32;
import static com.vcvinci.common.schema.CommonTypes.MEMORY_RECORD_BATCH;

/**
 * 发送消息请求
 * @author vinci
 * @date 2018-12-29 14:22
 */
public class SendRequest extends AbstractRequest {

    private static final Field ACKS_KEY = new Field(ProtocolSchemaConstant.ACKS_KEY_NAME, INT16, "参见AckType");
    private static final Field TIMEOUT_KEY = new Field(ProtocolSchemaConstant.TIMEOUT_KEY_NAME, INT32, "超时时间");
    private static final Field RECORD_BATCH_FIELD = new Field(ProtocolSchemaConstant.RECORD_BATCH, MEMORY_RECORD_BATCH, "消息内容");

    private static final Schema PRODUCE_REQUEST_V0 = new Schema(
            ACKS_KEY,
            TIMEOUT_KEY,
            CommonField.TOPIC_NAME,
            CommonField.PARTITION_ID,
            RECORD_BATCH_FIELD);

    private final short acks;

    public short getAcks() {
        return acks;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getTopic() {
        return topic;
    }

    public int getPartition() {
        return partition;
    }

    private final int timeout;
    private final String topic;
    private final int partition;
    private final MemoryRecordBatch recordBatch;

    public SendRequest(short version, short acks, int timeout, String topic, int partition, MemoryRecordBatch recordBatch) {
        super(RequestResponseMapper.Send, version);
        this.acks = acks;
        this.timeout = timeout;
        this.topic = topic;
        this.partition = partition;
        this.recordBatch = recordBatch;
    }

    public SendRequest(Struct struct, short version) {
        super(RequestResponseMapper.Send, version);
        acks = struct.getShort(ProtocolSchemaConstant.ACKS_KEY_NAME);
        timeout = struct.getInt(ProtocolSchemaConstant.TIMEOUT_KEY_NAME);
        topic = struct.getString(ProtocolSchemaConstant.TOPIC_KEY_NAME);
        partition = struct.getInt(ProtocolSchemaConstant.PARTITION_KEY_NAME);
        recordBatch = struct.getDefaultRecordBatch(ProtocolSchemaConstant.RECORD_BATCH);
    }

    public static Schema[] schemaVersions() {
        return new Schema[]{PRODUCE_REQUEST_V0};
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.Send.getRequestSchema(getVersion()));
        struct.set(ProtocolSchemaConstant.ACKS_KEY_NAME, acks);
        struct.set(ProtocolSchemaConstant.TIMEOUT_KEY_NAME, timeout);
        struct.set(ProtocolSchemaConstant.TOPIC_KEY_NAME, topic);
        struct.set(ProtocolSchemaConstant.PARTITION_KEY_NAME, partition);
        struct.set(ProtocolSchemaConstant.RECORD_BATCH, recordBatch);
        return struct;
    }

    @Override
    public AbstractResponse getErrorResponse(int throttleTime, Throwable e) {
        // TODO Auto-generated method stub
        return null;
    }

    public static SendRequest parse(ByteBuffer buffer, short version) {
        return new SendRequest(RequestResponseMapper.Send.parseRequest(version, buffer), version);
    }

    public MemoryRecordBatch getRecordBatch() {
        return this.recordBatch;
    }

    @Override
    public String toString() {
        return "SendRequest{"
                + "acks=" + acks
                + ", timeout=" + timeout
                + ", topic='" + topic + '\''
                + ", partition=" + partition
                + ", recordBatch=" + recordBatch
                + '}';
    }
}