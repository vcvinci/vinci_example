package com.vcvinci.common.exception.server;

/**
 * @Auther: yuxin.chen01
 * @Date: 2018/7/10 11:40
 * @Description: group正在负载均衡
 */
public class GroupNotEmptyException extends ServerException {

    public GroupNotEmptyException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public GroupNotEmptyException(String message) {
        super(message);
    }
}
