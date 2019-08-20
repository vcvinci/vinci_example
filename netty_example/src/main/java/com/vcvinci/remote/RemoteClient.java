package com.vcvinci.remote;

import com.vcvinci.protocol.request.Request;
import com.vcvinci.protocol.response.Response;
import com.vcvinci.remote.callback.RequestCompletionHandler;

import java.util.concurrent.TimeoutException;

/**
 * 客户端网络接口
 * @author vinci
 * @version 1.0 创建时间：2017-12-21 上午10:29:24
 */
public interface RemoteClient extends BaseRemote {

    /**
     * 功能描述: 同步发送消息
     * @param addr 地址 ip:port
     * @param request {@Request}的子类
     * @param timeoutMillis 超时毫秒数
     * @return  返回 {@link Response} 的子类
     * @throws  TimeoutException 如果 网络异常
     */
    Response invokeSync(final String addr, final Request request,
                        final long timeoutMillis) throws InterruptedException, TimeoutException;

    /**
     * 功能描述: 异步发送消息
     * @param addr 地址 ip:port
     * @param request {@Request}的子类
     * @param timeoutMillis 超时毫秒数
     * @param callback 异步返回的结果，回调函数
     * @throws  TimeoutException 如果 网络异常
     */
    void invokeAsync(final String addr, final Request request, final long timeoutMillis,
                     final RequestCompletionHandler callback) throws InterruptedException, TimeoutException;

    /**
     * 功能描述: 是否连接上
     * @param  host 地址 ip:port
     * @return boolean
     */
    boolean connected(String host);

    /**
     * 功能描述: 连接broker
     * @param  host 地址 ip:port
     * @param  timeout 超时时间
     */
    void connect(String host, long timeout) throws TimeoutException, InterruptedException;

    void connect(String host) throws TimeoutException, InterruptedException;

}
