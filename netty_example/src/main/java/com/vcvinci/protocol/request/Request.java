package com.vcvinci.protocol.request;

import com.vcvinci.protocol.response.Response;

/**
 * @author sunyupeng
 */
public interface Request extends RequestResponse {

    short getCode();

    short minVersion();

    short maxVersion();

    Response getErrorResponse(int throttleTime, Throwable e);
}
