package com.vcvinci.protocol.network;

import com.vcvinci.protocol.request.Header;
import com.vcvinci.protocol.request.RequestHeader;
import com.vcvinci.protocol.request.RequestResponse;
import com.vcvinci.protocol.util.ProcotolHelper;

import java.nio.ByteBuffer;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月24日 上午10:45:56
 * @description 类说明
 */
public class NetworkReceive {

    private String source;
    private Header header;
    private RequestResponse body;
    private boolean isResponse;
    private ByteBuffer bodyBuf;

    public NetworkReceive(String source, ByteBuffer buf) {
        this.source = source;
        byte flag = buf.get();
        isResponse = ProcotolHelper.isResponse(flag);
        this.header = isResponse ? ProcotolHelper.parseToResponseHeader(buf) : ProcotolHelper.parseToRequestHeader(buf);
        this.bodyBuf = buf;
    }

    public boolean isResponse() {
        return isResponse;
    }

    public int getId() {
        return this.header.getId();
    }

    public Header getHeader() {
        return this.header;
    }

    public RequestResponse buildBody(RequestHeader header) {
        return ProcotolHelper.parseBody(isResponse, header, bodyBuf);
    }
}
