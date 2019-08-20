/**
 * FileName: DoveExceptionResponse
 * Author:   HuangTaiHong
 * Date:     2019/2/3 9:11
 * Description: dove exception response.
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.vcvinci.protocol.response;

import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.util.Map;

/**
 * 〈dove exception response.〉
 *
 * @author HuangTaiHong
 * @create 2019/2/3
 * @since 1.0.0
 */
public class DoveExceptionResponse extends AbstractResponse {
    private static final Schema EXCEPTION_RESPONSE_V0 = new Schema();

    public static Schema[] schemaVersions() {
        return new Schema[]{EXCEPTION_RESPONSE_V0};
    }

    public DoveExceptionResponse(short version) {
        super(version);
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        return null;
    }

    @Override
    public Struct toStruct() {
        return new Struct(RequestResponseMapper.ExceptionResponse.getResponseSchema(getVersion()));
    }
}