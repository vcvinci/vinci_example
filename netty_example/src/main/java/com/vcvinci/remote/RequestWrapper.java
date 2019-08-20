package com.vcvinci.remote;

import com.vcvinci.protocol.network.NetworkSend;
import com.vcvinci.protocol.request.Header;
import com.vcvinci.protocol.response.Response;
import com.vcvinci.protocol.util.RequestResponseMapper;
import com.vcvinci.remote.callback.RequestFuture;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月22日 下午3:58:16
 * @description 类说明
 */
public class RequestWrapper {

    private NetworkSend send;
    private int requestType;
    private long requestTimeMs;
    private int timeout;
    private RequestResponseMapper mapper;
    private short code;
    private RequestFuture<Response> future;

    public RequestWrapper() {
        this.future = new RequestFuture<>();
    }

    public RequestWrapper(NetworkSend send, short code) {
        this();
        this.send = send;
        this.code = code;
    }

    public void onResponse(Response response) {
        future.completeAndSetResult(response);
    }

    public void setException(RuntimeException e) {
        future.completeAndSetException(e);
    }

    public int getTimeout() {
        return this.timeout;
    }

    public long getRequestTime() {
        return this.requestTimeMs;
    }

    public RequestFuture<Response> getFuture() {
        return this.future;
    }

    public Header getHeader() {
        return send.getHeader();
    }

    public short getCode() {
        return code;
    }

    public void setCode(short code) {
        this.code = code;
    }
}
