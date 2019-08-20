package com.vcvinci.protocol.network;

import com.vcvinci.common.schema.Struct;
import com.vcvinci.common.util.ProcotolConstant;
import com.vcvinci.protocol.request.Header;
import com.vcvinci.protocol.request.RequestResponse;
import com.vcvinci.protocol.response.Response;

import java.nio.ByteBuffer;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月9日 下午3:52:51
 * @description 网络传输单元，从本机的维度来看（对客户端的响应和来自客户端的请求都属于发送）
 */
public class NetworkSend {

    private String destination;
    /**
     * byte的第一位标示Send类型，0:request、1:response
     */
    private byte flag = 0x00;
    private Header header;
    private RequestResponse body;

    public NetworkSend(String destination, Header header, RequestResponse body) {
        this.header = header;
        this.body = body;
        if (body instanceof Response) {
            flag = (byte) (flag | ProcotolConstant.NETWORK_SEND_TYPE_FLAG);
        }
    }

    /**
     * TODO: 优化尝试减少内存拷贝，但是受限于模块依赖拓扑
     *
     * <pre>
     *                  common
     *            ^       ^        ^
     *          /         |          \
     * protocol   <-   remote   <-   store
     * </pre>
     */
    public ByteBuffer toByteBuffer() {
        Struct headerStruct = header.toStruct();
        Struct bodyStruct = body.toStruct();
        ByteBuffer buffer = ByteBuffer.allocate(1 + headerStruct.sizeOf() + bodyStruct.sizeOf());
        // 第一位：数据大小
        // buffer.putInt(headerStruct.sizeOf() + bodyStruct.sizeOf());
        // 请求类型，0:request；1:response
        buffer.put(flag);
        headerStruct.writeTo(buffer);
        bodyStruct.writeTo(buffer);
        buffer.flip();
        return buffer;
    }

    public String getDestination() {
        return destination;
    }

    public Header getHeader() {
        return header;
    }

    public RequestResponse getBody() {
        return body;
    }
}
