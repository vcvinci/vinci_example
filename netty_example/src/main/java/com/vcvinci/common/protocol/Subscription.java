package com.vcvinci.common.protocol;

import java.util.List;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 上午8:24:07
 * @description 类说明
 */
public class Subscription {

    private List<String> topics;

    public Subscription(List<String> topics) {
        this.topics = topics;
    }

    public List<String> topics() {
        return this.topics;
    }

}
 