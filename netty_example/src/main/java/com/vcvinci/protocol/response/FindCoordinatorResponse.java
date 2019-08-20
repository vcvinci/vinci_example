package com.vcvinci.protocol.response;

import com.vcvinci.common.cluster.Broker;
import com.vcvinci.common.exception.ErrorCodes;
import com.vcvinci.common.schema.CommonField;
import com.vcvinci.common.schema.Schema;
import com.vcvinci.common.schema.Struct;
import com.vcvinci.protocol.util.RequestResponseMapper;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月23日 上午11:30:42
 * @description 查找coordinator的响应类
 */
public class FindCoordinatorResponse extends AbstractResponse {

    private static final Schema FIND_COORDINATOR_RESPONSE_V0 = new Schema(
            CommonField.ERROR_CODE,
            CommonField.BROKER_ID,
            CommonField.HOST,
            CommonField.PORT);

    public static Schema[] schemaVersions() {
        return new Schema[]{FIND_COORDINATOR_RESPONSE_V0};
    }

    private final Broker broker;

    public FindCoordinatorResponse(short version, ErrorCodes error, Broker broker) {
        super(version, error);
        this.broker = broker;
    }

    public FindCoordinatorResponse(Struct struct, short version) {
        super(version, struct.get(CommonField.ERROR_CODE));
        int brokerId = struct.get(CommonField.BROKER_ID);
        String host = struct.get(CommonField.HOST);
        int port = struct.get(CommonField.PORT);
        this.broker = new Broker(brokerId, host, port);
    }

    @Override
    public Struct toStruct() {
        Struct struct = new Struct(RequestResponseMapper.FindCoordinator.getResponseSchema(getVersion()));
        struct.set(CommonField.ERROR_CODE, error.code());
        struct.set(CommonField.BROKER_ID, this.broker.getId());
        struct.set(CommonField.HOST, this.broker.getHost());
        struct.set(CommonField.PORT, this.broker.getPort());
        return struct;
    }

    @Override
    public Map<ErrorCodes, Integer> errorCounts() {
        // TODO Auto-generated method stub
        return null;
    }

    public Broker coordinator(){
        return this.broker;
    }

    public ErrorCodes errorCodes(){
        return this.error;
    }

    public static FindCoordinatorResponse parse(ByteBuffer buffer, short version) {
        return new FindCoordinatorResponse(RequestResponseMapper.FindCoordinator.parseResponse(version, buffer), version);
    }
}
 