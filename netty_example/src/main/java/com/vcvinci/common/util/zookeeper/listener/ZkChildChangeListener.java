package com.vcvinci.common.util.zookeeper.listener;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月5日 下午1:34:03
 * @description 类说明
 */
public interface ZkChildChangeListener {

    String path();

    void handleChildChange();
}
 