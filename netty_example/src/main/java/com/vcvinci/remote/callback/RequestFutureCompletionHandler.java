package com.vcvinci.remote.callback;

import com.vcvinci.protocol.response.ResponseHeader;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月5日 上午10:42:30
 * @description 类说明
 */
public class RequestFutureCompletionHandler<T> implements RequestCompletionHandler<T> {

    private RequestFuture<T> future;
    private T response;
    private RuntimeException exception;

    public RequestFutureCompletionHandler() {
        future = new RequestFuture<T>();
    }

    public void fireComplete() {

        if(exception == null){
            future.completeAndSetResult(response);
        }else{
            future.completeAndSetException(exception);
        }
    }

    @Override
    public void onComplete(T response, ResponseHeader header) {
        this.response = response;
    }

    @Override
    public void onFailure(RuntimeException e) {
        this.exception = e;
    }

    public RequestFuture<T> getFuture(){
        return this.future;
    }
}
