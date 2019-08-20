package com.vcvinci.remote.processor;

import com.vcvinci.protocol.request.Request;
import com.vcvinci.protocol.request.RequestHeader;
import io.netty.channel.Channel;

/**
 * netty请求处理器
 * @author yupeng.sun@ucarinc.com
 * @version 1.0 创建时间：2017-12-21 上午10:29:01
 */
public interface NettyRequestProcessor<T extends Request> {

    /**
     * 功能描述:
     * @param request {@link Request} 的子类
     * @param header 请求头
     * @param channel netty封装的Channel
     * @throws Exception 网络问题
     */
    void processRequest(T request, final RequestHeader header, final Channel channel) throws Exception;

}
