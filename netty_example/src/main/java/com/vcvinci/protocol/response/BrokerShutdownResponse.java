package com.vcvinci.protocol.response;

import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.schema.Struct;

import java.util.Map;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月23日 上午11:12:57
 * @description 类说明
 */
public class BrokerShutdownResponse extends AbstractResponse {

    public BrokerShutdownResponse(short version) {
        super(version);
    }

    @Override
    public Struct toStruct() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        // TODO Auto-generated method stub
        return null;
    }

}
