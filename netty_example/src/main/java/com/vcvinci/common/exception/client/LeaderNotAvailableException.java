package com.vcvinci.common.exception.client;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @date 2018年6月27日 下午3:40:20
 * @description 类说明
 */

public class LeaderNotAvailableException extends ClientException {

    private static final long serialVersionUID = 1L;

    public LeaderNotAvailableException() {
    }

    public LeaderNotAvailableException(String message) {
        super(message);
    }

    public LeaderNotAvailableException(Throwable cause) {
        super(cause);
    }

    public LeaderNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}