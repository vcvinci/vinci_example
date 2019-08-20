package com.vcvinci.common.exception.server;

/**
 * @CreateDate: 2018/11/19 11:10
 * @Author: zhengquan.lin@ucarinc.com
 * @Description:
 */
public class GroupNameNotFoundException extends ServerException {

    public GroupNameNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public GroupNameNotFoundException(String message) {
        super(message);
    }
}
