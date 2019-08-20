package com.vcvinci.remote.callback;

import com.vcvinci.protocol.response.ResponseHeader;

/**
 * 类说明：请求完成响应
 * @author vinci
 * @version 创建时间：2018年5月22日 下午4:00:53
 */
public interface RequestCompletionHandler<T> {

	/**
	 * 功能描述:
	 * @param response subclass of this response
	 * @param header 响应头
	 * @date 2018/11/26 下午5:52
	 */
	void onComplete(T response, ResponseHeader header);

	/**
	 * 功能描述: 请求结果出现异常得返回
	 * @date 2018/7/4 17:35
	 */
	void onFailure(RuntimeException e);

}


