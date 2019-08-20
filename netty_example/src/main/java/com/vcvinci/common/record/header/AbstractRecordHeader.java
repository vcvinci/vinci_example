package com.vcvinci.common.record.header;

import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.CommonTypes.STRUCT;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProcotolConstant;
import com.vcvinci.common.util.ProtocolSchemaConstant;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version v1
 * @date 2018年7月17日 上午9:27:19
 * @description TODO
 */
public abstract class AbstractRecordHeader implements RecordHeader {

    private static final Schema COMMON_SERVER_HEADER_VO = new Schema(
            CommonField.FLAG,
            CommonField.VERSION,
            CommonField.STORE_TIME,
            CommonField.OFFSET,
            CommonField.LENGTH);

    private static final Schema COMMON_CLIENT_HEADER_VO = new Schema(
            CommonField.FLAG,
            CommonField.VERSION,
            CommonField.LENGTH);

    protected static final Field COMMON_SERVER_RECORD_HEADER_VO = new Field(
            ProtocolSchemaConstant.COMMON_SERVER_HEADER_KEY_NAME, new STRUCT(COMMON_SERVER_HEADER_VO));

    protected static final Field COMMON_CLIENT_RECORD_HEADER_VO = new Field(
            ProtocolSchemaConstant.COMMON_CLIENT_HEADER_KEY_NAME, new STRUCT(COMMON_CLIENT_HEADER_VO));

    private final short version;
    private final byte flag;
    private final long storeTime;
    private final long offset;
    private int length = -1;
    private int headerLength = -1;

    public AbstractRecordHeader(short version, byte flag, long storeTime, long offset) {
        this.version = version;
        this.storeTime = storeTime;
        if (storeTime != HAS_NO_STORE_TIME) {
            this.flag = (byte) (flag | ProcotolConstant.HAS_STORE_TIME_FLAG);
        } else {
            this.flag = flag;
        }
        this.offset = offset;
    }

    protected AbstractRecordHeader(Struct st) {
        Struct struct;
        if (st.hasField(ProtocolSchemaConstant.COMMON_CLIENT_HEADER_KEY_NAME)) {
            struct = st.getStruct(ProtocolSchemaConstant.COMMON_CLIENT_HEADER_KEY_NAME);
            this.storeTime = HAS_NO_STORE_TIME;
            this.offset = HAS_NO_OFFSET;
        } else {
            struct = st.getStruct(ProtocolSchemaConstant.COMMON_SERVER_HEADER_KEY_NAME);
            this.storeTime = struct.get(CommonField.STORE_TIME);
            this.offset = struct.get(CommonField.OFFSET);
        }
        this.version = struct.get(CommonField.VERSION);
        this.flag = struct.get(CommonField.FLAG);
        this.length = struct.get(CommonField.LENGTH);
    }

    protected Struct generateStruct(Schema schema, String structName) {
        Struct struct = new Struct(schema);
        Struct simpleRecordHeaderStruct = struct.instance(structName);
        setCommonHeaderStruct(simpleRecordHeaderStruct);
        struct.set(structName, simpleRecordHeaderStruct);
        return struct;
    }

    private void setCommonHeaderStruct(Struct struct) {
        if (hasStoreTime()) {
            Struct commonServerHeaderStruct = struct.instance(ProtocolSchemaConstant.COMMON_SERVER_HEADER_KEY_NAME);
            commonServerHeaderStruct.set(CommonField.FLAG, getFlag());
            commonServerHeaderStruct.set(CommonField.VERSION, getVersion());
            commonServerHeaderStruct.set(CommonField.STORE_TIME, getStoreTime());
            commonServerHeaderStruct.set(CommonField.OFFSET, this.getOffset());
            commonServerHeaderStruct.set(CommonField.LENGTH, this.length);
            struct.set(ProtocolSchemaConstant.COMMON_SERVER_HEADER_KEY_NAME, commonServerHeaderStruct);
        } else {
            Struct commonClientHeaderStruct = struct.instance(ProtocolSchemaConstant.COMMON_CLIENT_HEADER_KEY_NAME);
            commonClientHeaderStruct.set(CommonField.FLAG, getFlag());
            commonClientHeaderStruct.set(CommonField.VERSION, getVersion());
            commonClientHeaderStruct.set(CommonField.LENGTH, this.length);
            struct.set(ProtocolSchemaConstant.COMMON_CLIENT_HEADER_KEY_NAME, commonClientHeaderStruct);
        }
    }

    public final boolean hasStoreTime() {
        return hasStoreTime(this.flag);
    }

    protected static boolean hasStoreTime(byte flag) {
        return (flag & ProcotolConstant.HAS_STORE_TIME_FLAG) == ProcotolConstant.HAS_STORE_TIME_FLAG;
    }

    public short getVersion() {
        return version;
    }

    public byte getFlag() {
        return flag;
    }

    public long getStoreTime() {
        return storeTime;
    }

    public long getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public int getHeaderLength() {
        return this.headerLength;
    }

    // 这块代码写的好恶心。。。。
    @SuppressWarnings("unchecked")
    public void setAllLength(Struct headerStruct) {
        this.headerLength = headerStruct.sizeOf();

        int skipOffset = 0;
        skipOffset += CommonField.FLAG.getType().sizeOf(this.flag);
        skipOffset += CommonField.VERSION.getType().sizeOf(this.version);
        skipOffset += CommonField.LENGTH.getType().sizeOf(this.length);

        this.length = headerLength - skipOffset;
        // 设置长度字段（长度字段不使用变长）。
        if (hasStoreTime()) {
            headerStruct.getStruct(ProtocolSchemaConstant.COMMON_SERVER_HEADER_KEY_NAME).set(CommonField.LENGTH, length);
        } else {
            headerStruct.getStruct(ProtocolSchemaConstant.COMMON_CLIENT_HEADER_KEY_NAME).set(CommonField.LENGTH, length);
        }
    }

}
