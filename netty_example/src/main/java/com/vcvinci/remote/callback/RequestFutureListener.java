package com.vcvinci.remote.callback;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月22日 下午8:30:23
 * @description 类说明
 */
public interface RequestFutureListener<T> {

    void onSuccess(T value);

    void onFailure(RuntimeException e);

}