package com.vcvinci.common.exception.server;

import com.vcvinci.common.exception.RetriableException;

/**
 * @CreateDate: 2018/12/22 10:09
 * @Author: zhengquan.lin@ucarinc.com
 * @Description:
 * 协调器处于正在加载：
 * 有两种情况:
 * >    1、 broker 重启恢复groupMetadata、offsetMetadata
 * >    2、 分区重新分配, 加载其他broker转移过来的 groupMetadata、offsetMetadata
 */
public class CoordinatorLoadInProgressException extends ServerException implements RetriableException{

    public CoordinatorLoadInProgressException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public CoordinatorLoadInProgressException(String message) {
        super(message);
    }
}
