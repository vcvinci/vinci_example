package com.vcvinci.protocol.response;

import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.protocol.request.RequestResponse;

import java.util.Map;

public interface Response extends RequestResponse {

    Map<ErrorCodes, Integer> errorCounts();

}
