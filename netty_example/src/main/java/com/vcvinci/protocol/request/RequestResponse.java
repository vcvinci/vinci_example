package com.vcvinci.protocol.request;

import com.vcvinci.common.Structable;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月9日 下午3:08:50
 */
public interface RequestResponse extends Structable {

    /**
     * 功能描述: 获取请求响应的版本号
     * @return 版本号
     * @author vinci
     * @date 2018/12/3 下午1:58
     */
    short getVersion();

}
 