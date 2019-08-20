package com.vcvinci.common.exception.server;

/**
 * @Auther: yuxin.chen01
 * @Date: 2018/7/9 20:31
 * @Description: 组成员指定的分区分配协议与组内其他成员不兼容
 */
public class InconsistentGroupProtocolException extends ServerException {

    public InconsistentGroupProtocolException(String message) {
        super(message);
    }

    public InconsistentGroupProtocolException(String message, Throwable cause) {
        super(message, cause);
    }
}
