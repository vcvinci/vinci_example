package com.vcvinci.protocol.response;

import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.protocol.request.AbstractRequestResponse;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月9日 下午3:14:41
 * @description 类说明
 */
public abstract class AbstractResponse extends AbstractRequestResponse implements Response {

    protected ErrorCodes error;

    public AbstractResponse(short version) {
        this(version, ErrorCodes.NONE);
    }

    public AbstractResponse(short version, short errorCode) {
        this(version, ErrorCodes.forCode(errorCode));
    }

    public AbstractResponse(short version, ErrorCodes error) {
        super(version);
        this.error = error;
    }

    public void setError(ErrorCodes error) {
        this.error = error;
    }

    public ErrorCodes getError() {
        return this.error;
    }
}
