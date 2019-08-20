package com.vcvinci.protocol.request;

import com.vcvinci.common.schema.Struct;
import com.vcvinci.protocol.response.AbstractResponse;
import com.vcvinci.protocol.util.RequestResponseMapper;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 上午8:14:04
 * @description 类说明
 */
public class BrokerShutdownRequest extends AbstractRequest {

    public BrokerShutdownRequest(RequestResponseMapper mapper, short version) {
        super(mapper, version);
        // TODO Auto-generated constructor stub
    }

    private int brokerId;

    @Override
    public Struct toStruct() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractResponse getErrorResponse(int throttleTime, Throwable e) {
        // TODO Auto-generated method stub
        return null;
    }

}
 