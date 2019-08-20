package com.vcvinci.protocol.request;

import com.vcvinci.protocol.util.RequestResponseMapper;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 上午8:05:51
 * @description 类说明
 */
public abstract class ControllerRequest extends AbstractRequest {

    private int controllerId;
    private int controllerEpoch;

    public ControllerRequest(int controllerId, int controllerEpoch, RequestResponseMapper mapper, short version) {
        super(mapper, version);
        this.controllerId = controllerId;
        this.controllerEpoch = controllerEpoch;
    }

    public ControllerRequest(RequestResponseMapper mapper, short version) {
        super(mapper, version);
    }

    public int controllerId() {
        return this.controllerId;
    }

    public int controllerEpoch() {
        return this.controllerEpoch;
    }
}
 