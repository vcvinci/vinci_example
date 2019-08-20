/**
 * FileName: ApiError
 * Author:   HuangTaiHong
 * Date:     2018/12/1 11:53
 * Description: 封装错误码与错误消息
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.vcvinci.protocol.request;

import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProtocolSchemaConstant;

/**
 * 〈一句话功能简述〉<br>
 * 〈封装错误码与错误消息〉
 *
 * @author HuangTaiHong
 * @create 2018/12/1
 * @since 1.0.0
 */
public class ApiError {
    private final String message;
    private final ErrorCodes errorCodes;

    public static final ApiError NONE = new ApiError(ErrorCodes.NONE, null);

    public ApiError(Struct struct) {
        message = struct.getString(ProtocolSchemaConstant.ERROR_MESSAGE_KEY_NAME);
        errorCodes = ErrorCodes.forCode(struct.getShort(ProtocolSchemaConstant.ERROR_CODE_KEY_NAME));
    }

    public ApiError(ErrorCodes errorCodes, String message) {
        this.message = message;
        this.errorCodes = errorCodes;
    }

    public static ApiError fromThrowable(Throwable t) {
        ErrorCodes error = ErrorCodes.forException(t);
        String message = error.getDefaultErrMsg().equals(t.getMessage()) ? null : t.getMessage();
        return new ApiError(error, message);
    }

    public boolean isSuccess() {
        return is(ErrorCodes.NONE);
    }

    public boolean is(ErrorCodes error) {
        return this.errorCodes == error;
    }

    public String getMessage() {
        return message;
    }

    public ErrorCodes getErrorCodes() {
        return errorCodes;
    }
}