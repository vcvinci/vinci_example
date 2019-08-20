package com.vcvinci.common.util.zookeeper.listener;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月5日 下午1:33:20
 * @description 类说明
 */
public interface ZkDataChangListener {

    String path();

    void handleCreation();

    void handleDeletion();

    void handleDataChange();
}
 