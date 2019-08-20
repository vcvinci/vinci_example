package com.vcvinci.common.exception.client;

import com.vcvinci.common.exception.RetriableException;

/**
 * @Auther: yuxin.chen01
 * @Date: 2018/12/7 14:24
 * @Description: 在加入组之前member的状态发生了改变, 客户端会抛出该异常, 进行重试
 */
public class UnjoinedGroupException extends ClientException implements RetriableException {
}
