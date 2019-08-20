package com.vcvinci.common.util.zookeeper.listener;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月5日 下午1:34:29
 * @description 类说明
 */
public interface ZkStateChangeListener {

    String getName();

    void beforeInitializingSession();

    void afterInitializingSession();

    void onAuthFailure();
}
 