package com.vcvinci.exception.delivery;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年6月9日 下午6:04:04
 * @description 类说明
 */
public class ClientException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientException(String message) {
		super(message);
	}

	public ClientException(Throwable cause) {
		super(cause);
	}

	public ClientException() {
		super();
	}

}
