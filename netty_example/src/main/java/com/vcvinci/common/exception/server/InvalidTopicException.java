package com.vcvinci.common.exception.server;

/**
 * @CreateDate: 2018/11/16 9:57
 * @Author: zhengquan.lin@ucarinc.com
 * @Description:
 */
public class InvalidTopicException extends ServerException {

    private static final long serialVersionUID = 1L;

    public InvalidTopicException() {
        super();
    }

    public InvalidTopicException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTopicException(String message) {
        super(message);
    }

    public InvalidTopicException(Throwable cause) {
        super(cause);
    }

}
