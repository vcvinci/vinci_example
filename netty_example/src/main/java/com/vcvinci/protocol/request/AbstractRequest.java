package com.vcvinci.protocol.request;

import com.vcvinci.protocol.util.RequestResponseMapper;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月9日 下午3:12:00
 * @description 类说明
 */
public abstract class AbstractRequest extends AbstractRequestResponse implements Request {

    protected RequestResponseMapper mapper;

    public AbstractRequest(RequestResponseMapper mapper, short version) {
        super(version);
        this.mapper = mapper;
    }

    @Override
    public short getCode() {
        return mapper.getCode();
    }

    @Override
    public short minVersion() {
        return mapper.minVersion();
    }

    @Override
    public short maxVersion() {
        return mapper.maxVersion();
    }
}
