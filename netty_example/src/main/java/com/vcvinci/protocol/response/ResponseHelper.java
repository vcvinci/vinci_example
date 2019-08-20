package com.vcvinci.protocol.response;

import com.vcvinci.protocol.request.Request;

/**
 * @author yupeng.sun@ucarinc.com
 * @version 创建时间：2018年1月25日 下午4:37:21
 * @Description
 */
public class ResponseHelper {

    public static Response createErrorResponseByRequest(Request request, short statusCode) {
//		short code = (short) (request.getCode() + 1);
//		switch (code) {
//		case 0:
//			return new SendResponse(request.getVersion(), request.getId(), statusCode, 0, (short) 0);
//		case 1:
//		default:
//			return null;
//		}
        return null;
    }

}
