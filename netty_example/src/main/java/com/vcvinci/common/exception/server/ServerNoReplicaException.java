package com.vcvinci.common.exception.server;

/**
 * 服务端找不到指定的replica
 *
 * @author yankai.zhang
 * @since 2018/7/11 下午5:20
 */
public class ServerNoReplicaException extends ServerException {

    public ServerNoReplicaException(String message) {
        super(message);
    }

    public ServerNoReplicaException(String message, Throwable cause) {
        super(message, cause);
    }
}
