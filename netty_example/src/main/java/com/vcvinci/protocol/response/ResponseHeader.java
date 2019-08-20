package com.vcvinci.protocol.response;

import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.request.Header;

import java.nio.ByteBuffer;

import static com.vcvinci.common.schema.CommonTypes.INT16;
import static com.vcvinci.common.schema.CommonTypes.INT32;

public class ResponseHeader implements Header {

    private static final Field API_VERSION = new Field(ProtocolSchemaConstant.API_VERSION_KEY_NAME, INT16, "The version of the API.");
    private static final Field ID = new Field(ProtocolSchemaConstant.ID_KEY_NAME, INT32,
            "A user-supplied integer value that will be passed back with the response");

    public static final Schema SCHEMA = new Schema(API_VERSION, ID);

    private final short apiVersion;
    private final int id;

    public ResponseHeader(Struct struct) {
        this.apiVersion = struct.getShort(ProtocolSchemaConstant.API_VERSION_KEY_NAME);
        this.id = struct.getInt(ProtocolSchemaConstant.ID_KEY_NAME);
    }

    public ResponseHeader(short version, int id) {
        this.apiVersion = version;
        this.id = id;
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(SCHEMA);
        struct.set(ProtocolSchemaConstant.API_VERSION_KEY_NAME, apiVersion);
        struct.set(ProtocolSchemaConstant.ID_KEY_NAME, id);
        return struct;
    }

    public static ResponseHeader parse(ByteBuffer buffer) {
        try {
            Schema schema = SCHEMA;
            return new ResponseHeader(schema.read(buffer));
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public short getApiVersion() {
        return apiVersion;
    }

    @Override
    public int getId() {
        return id;
    }
}
