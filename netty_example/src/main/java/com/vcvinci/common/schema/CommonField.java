package com.vcvinci.common.schema;

import com.vcvinci.common.util.ProtocolSchemaConstant;

/**
 * @author yupeng.sun@ucarinc.com
 * @version 1.0 创建时间：2017-12-14 下午6:05:15
 * @Description:
 */
public class CommonField {

    public static final Int32 PARTITION_ID = new Int32(ProtocolSchemaConstant.PARTITION_KEY_NAME, "Topic partition id");
    public static final Int64 OFFSET = new Int64(ProtocolSchemaConstant.OFFSET_KEY_NAME, "offset");
    public static final Str TOPIC_NAME = new Str(ProtocolSchemaConstant.TOPIC_KEY_NAME, "topic名称");
    public static final Str GROUP_NAME = new Str(ProtocolSchemaConstant.GROUP_KEY_NAME, "group名称");
    public static final Str MEMBER_ID = new Str(ProtocolSchemaConstant.MEMBER_ID_KEY_NAME, "memberId");
    public static final Int32 GENERATION_ID = new Int32(ProtocolSchemaConstant.GENERATION_ID_KEY_NAME, "generation id");
    public static final Str METADATA = new Str(ProtocolSchemaConstant.METADATA_KEY_NAME, "metadata");
    public static final Int32 METADATA_VERSION = new Int32(ProtocolSchemaConstant.METADATA_VERSION_KEY_NAME, "metadata_version");
    public static final Int64 TIMESTAMP = new Int64(ProtocolSchemaConstant.TIMESTAMP_KEY_NAME, "timestamp");
    public static final Int16 ERROR_CODE = new Int16(ProtocolSchemaConstant.ERROR_CODE_KEY_NAME, "响应错误码");
    public static final Int32 BROKER_ID = new Int32(ProtocolSchemaConstant.BROKER_ID_KEY_NAME, "broker id");
    public static final Str HOST = new Str(ProtocolSchemaConstant.HOST_KEY_NAME, "host");
    public static final Int32 PORT = new Int32(ProtocolSchemaConstant.PORT_KEY_NAME, "port");
    public static final Int64 STORE_TIME = new Int64(ProtocolSchemaConstant.STORE_TIME_NAME, "存储时间");
    public static final Int16 DELAY_TIME = new Int16(ProtocolSchemaConstant.DELAY_TIME_NAME, "延迟时间");
    public static final Int32 CONTROLLER_ID = new Int32(ProtocolSchemaConstant.CONTROLLER_ID_KEY_NAME, "controller id");
    public static final Int32 CONTROLLER_EPOCH = new Int32(ProtocolSchemaConstant.CONTROLLER_EPOCH_KEY_NAME, "controller epoch");
    public static final Int32 LEADER_ID = new Int32(ProtocolSchemaConstant.LEADER_KEY_NAME, "partition leader id");
    public static final Int32 LEADER_EPOCH = new Int32(ProtocolSchemaConstant.LEADER_EPOCH, "partition leader epoch");
    public static final Int32 REPLICA_ID = new Int32(ProtocolSchemaConstant.REPLICA_ID_KEY_NAME, "replica id");
    public static final Int32 MAX_WAIT_TIME_MS = new Int32(ProtocolSchemaConstant.MAX_WAIT_TIME_MS_KEY_NAME, "Minimum bytes to accumulate in the response.");
    public static final Int64 FETCH_OFFSET = new Int64(ProtocolSchemaConstant.FETCH_OFFSET_KEY_NAME, "Message offset.");
    public static final Int64 HIGH_WATERMARK = new Int64(ProtocolSchemaConstant.HIGH_WATERMARK_KEY_NAME, "Last committed offset.");
    public static final Int64 LOG_START_OFFSET = new Int64(ProtocolSchemaConstant.LOG_START_OFFSET_KEY_NAME, "Earliest available offset of the follower replica. The field is only used when request is sent by follower. ");
    public static final Int32 MIN_BYTES = new Int32(ProtocolSchemaConstant.MIN_BYTES_KEY_NAME, "Minimum bytes to accumulate in the response.");
    public static final Int32 MAX_BYTES = new Int32(ProtocolSchemaConstant.MAX_BYTES_KEY_NAME, "Maximum bytes to fetch.");

    public static final Int8 FLAG = new Int8(ProtocolSchemaConstant.FLAG_KEY_NAME, "flag");
    public static final Int16 VERSION = new Int16(ProtocolSchemaConstant.VERSION_KEY_NAME, "version");
    public static final Int32 LENGTH = new Int32(ProtocolSchemaConstant.LENGTH_KEY_NAME, "length");

    public static class Int32 extends Field {

        public Int32(String name, String docString) {
            super(name, CommonTypes.INT32, null, docString);
        }

        public Int32(String name, String docString, int defaultValue) {
            super(name, CommonTypes.INT32, defaultValue, docString);
        }
    }

    public static class Int16 extends Field {

        public Int16(String name, String docString) {
            super(name, CommonTypes.INT16, null, docString);
        }

        public Int16(String name, String docString, int defaultValue) {
            super(name, CommonTypes.INT16, defaultValue, docString);
        }
    }

    public static class Int8 extends Field {

        public Int8(String name, String docString) {
            super(name, CommonTypes.INT8, null, docString);
        }

        public Int8(String name, String docString, int defaultValue) {
            super(name, CommonTypes.INT8, defaultValue, docString);
        }
    }

    public static class Int64 extends Field {

        public Int64(String name, String docString) {
            super(name, CommonTypes.INT64, null, docString);
        }

        public Int64(String name, String docString, long defaultValue) {
            super(name, CommonTypes.INT64, defaultValue, docString);
        }
    }

    public static class Str extends Field {

        public Str(String name, String docString) {
            super(name, CommonTypes.STRING, null, docString);
        }
    }

}
