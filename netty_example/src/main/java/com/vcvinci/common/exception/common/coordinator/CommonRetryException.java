package com.vcvinci.common.exception.common.coordinator;

import com.vcvinci.common.exception.RetriableException;
import com.vcvinci.common.exception.common.CommonException;

/**
 * @Auther: yuxin.chen01
 * @Date: 2018/12/20 17:46
 * @Description:可重试异常, 主要考虑到有些异常实现retriableException可能影响范围太大, 所以可以用该异常包装下
 */
public class CommonRetryException extends CommonException implements RetriableException {

    private static final long serialVersionUID = 1L;

    public CommonRetryException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonRetryException(String message) {
        super(message);
    }

    public CommonRetryException(Throwable cause) {
        super(cause);
    }

    public CommonRetryException() {
        super();
    }
}
