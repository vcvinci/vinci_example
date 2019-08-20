package com.vcvinci.common.record.header;

import com.vcvinci.common.schema.CommonField.Int32;
import com.vcvinci.common.schema.CommonField.Int64;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version v1
 * @date 2018年7月17日 上午9:28:30
 * @description TODO
 */
public class SimpleRecordHeader extends AbstractRecordHeader {

    private final int crc32;
    private final long clientTime;
    private final long messageId;

    private static final Int32 CRC32 = new Int32(ProtocolSchemaConstant.CRC_KEY_NAME, "crc32");
    private static final Int64 CLIENT_TIME = new Int64(ProtocolSchemaConstant.CLIENT_TIME_KEY_NAME, "client time");
    private static final Int64 MESSAGE_ID = new Int64(ProtocolSchemaConstant.MESSAGE_ID_KEY_NAME, "message id");

    public static final Schema SERVER_SIMPLE_RECORD_HEADER_V0 = new Schema(
            COMMON_SERVER_RECORD_HEADER_VO,
            CRC32,
            CLIENT_TIME,
            MESSAGE_ID);

    public static final Schema CLIENT_SIMPLE_RECORD_HEADER_V0 = new Schema(
            COMMON_CLIENT_RECORD_HEADER_VO,
            CRC32,
            CLIENT_TIME,
            MESSAGE_ID);

    public SimpleRecordHeader(short version, byte flag, long storeTime, long offset, int crc32, long clientTime, long messageId) {
        super(version, flag, storeTime, offset);
        this.crc32 = crc32;
        this.clientTime = clientTime;
        this.messageId = messageId;
    }

    public SimpleRecordHeader(Struct struct) {
        super(struct);
        this.crc32 = struct.get(CRC32);
        this.clientTime = struct.get(CLIENT_TIME);
        this.messageId = struct.get(MESSAGE_ID);
    }

    @Override
    public Struct generateStruct(Schema schema) {
        String structName;
        if (hasStoreTime()) {
            structName = ProtocolSchemaConstant.SERVER_SIMPLE_RECORD_HEADER_KEY_NAME;
        } else {
            structName = ProtocolSchemaConstant.CLIENT_SIMPLE_RECORD_HEADER_KEY_NAME;
        }
        Struct struct = generateStruct(schema, structName);

        Struct simpleRecordHeaderStruct = struct.getStruct(structName);
        simpleRecordHeaderStruct.set(CRC32, crc32);
        simpleRecordHeaderStruct.set(CLIENT_TIME, this.clientTime);
        simpleRecordHeaderStruct.set(MESSAGE_ID, this.messageId);

        return struct;
    }

    public int getCrc32() {
        return crc32;
    }

    public long getClientTime() {
        return clientTime;
    }

    public long getMessageId() {
        return messageId;
    }

}
