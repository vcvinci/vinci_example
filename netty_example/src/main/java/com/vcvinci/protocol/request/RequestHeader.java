package com.vcvinci.protocol.request;

import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;

import java.nio.ByteBuffer;

import static com.vcvinci.common.schema.CommonTypes.INT16;
import static com.vcvinci.common.schema.CommonTypes.INT32;

/**
 * @author yupeng.sun@ucarinc.com
 * @version 1.0 创建时间：2017-12-19 下午3:53:06
 * @Description:
 */
public class RequestHeader implements Header {

    private static final Field CODE = new Field(ProtocolSchemaConstant.CODE_KEY_NAME, INT16,
            "The id of the request type.");
    private static final Field API_VERSION = new Field(
            ProtocolSchemaConstant.API_VERSION_KEY_NAME, INT16, "The version of the API.");
    private static final Field ID = new Field(ProtocolSchemaConstant.ID_KEY_NAME, INT32,
            "A user-supplied integer value that will be passed back with the response");

    public static final Schema SCHEMA = new Schema(CODE, API_VERSION, ID);
    //TODO
//	private final byte flag;
    private final short code;
    private final short apiVersion;
    private final int id;

    public RequestHeader(Struct struct) {
        this.code = struct.getShort(ProtocolSchemaConstant.CODE_KEY_NAME);
        this.apiVersion = struct.getShort(ProtocolSchemaConstant.API_VERSION_KEY_NAME);
        this.id = struct.getInt(ProtocolSchemaConstant.ID_KEY_NAME);
    }

    public RequestHeader(short code, short version, int id) {
        this.code = code;
        this.apiVersion = version;
        this.id = id;
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(SCHEMA);
        struct.set(ProtocolSchemaConstant.CODE_KEY_NAME, code);
        struct.set(ProtocolSchemaConstant.API_VERSION_KEY_NAME, apiVersion);
        struct.set(ProtocolSchemaConstant.ID_KEY_NAME, id);
        return struct;
    }

    public static RequestHeader parse(ByteBuffer buffer) {
        try {
            Schema schema = SCHEMA;
            return new RequestHeader(schema.read(buffer));
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public short getCode() {
        return code;
    }

    public short getApiVersion() {
        return apiVersion;
    }

    @Override
    public int getId() {
        return id;
    }
}
