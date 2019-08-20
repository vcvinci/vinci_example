/**
 * FileName: CreateTopicsResponse
 * Author:   HuangTaiHong
 * Date:     2018/12/1 11:37
 * Description: 创建Topic响应
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.vcvinci.protocol.response;

import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.schema.CommonTypes;
import com.vcvinci.common.schema.Field;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;
import com.vcvinci.protocol.request.ApiError;

import java.util.Map;

import static com.vcvinci.common.schema.CommonTypes.INT16;
import static com.vcvinci.common.schema.CommonTypes.NULLABLE_STRING;
import static com.vcvinci.common.schema.CommonTypes.STRING;

/**
 * 〈一句话功能简述〉<br>
 * 〈创建Topic响应〉
 *
 * @author HuangTaiHong
 * @create 2018/12/1
 * @since 1.0.0
 */
public class CreateTopicsResponse extends AbstractResponse {
    private static final Schema TOPIC_ERROR = new Schema(
            new Field(ProtocolSchemaConstant.TOPIC_NAME_KEY_NAME, STRING, "Name of topic"),
            new Field(ProtocolSchemaConstant.ERROR_CODE_KEY_NAME, INT16, "Response error code"),
            new Field(ProtocolSchemaConstant.ERROR_MESSAGE_KEY_NAME, NULLABLE_STRING, "Response error message"));

    private static final Schema CREATE_TOPICS_RESPONSE = new Schema(new Field(ProtocolSchemaConstant.TOPIC_ERRORS_KEY_NAME, new CommonTypes.ARRAY<>(TOPIC_ERROR), "An array of per topic errors."));

    private final Map<String, ApiError> errors;

    public static Schema[] schemaVersions() {
        return new Schema[]{CREATE_TOPICS_RESPONSE};
    }

    public CreateTopicsResponse(short version, Map<String, ApiError> errors) {
        super(version);
        this.errors = errors;
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        return null;
    }


    @Override
    public Struct toStruct() {
        return null;
    }

    public Map<String, ApiError> getErrors() {
        return errors;
    }
}