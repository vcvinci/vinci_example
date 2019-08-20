package com.vcvinci.protocol.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月9日 下午3:09:45
 * @description 类说明
 */
public abstract class AbstractRequestResponse implements RequestResponse {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private short version;

    public AbstractRequestResponse(short version) {
        this.version = version;
    }

    @Override
    public short getVersion() {
        return this.version;
    }

}
 