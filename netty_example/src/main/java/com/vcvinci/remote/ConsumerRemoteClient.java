package com.vcvinci.remote;

import com.vcvinci.common.Condition;
import com.vcvinci.common.cluster.Broker;
import com.vcvinci.protocol.request.Request;
import com.vcvinci.remote.callback.RequestFutureCompletionHandler;

import java.util.List;
import java.util.Map;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月5日 上午10:40:18
 * @description 类说明
 */
public class ConsumerRemoteClient {

    private CommonRemoteClient client;
    private List<RequestFutureCompletionHandler> completeRequests;
    private Map<Integer, List<Request>> unsents;

    public ConsumerRemoteClient(CommonRemoteClient client) {
        this.client = client;
    }

    public void awaitMetadataUpdate(int timeout) {

    }

    public void fireCompleteRequests() {

    }

    public void trySends() {

    }

    public void failExipireRequests() {

    }

    public void checkDisconncts() {

    }

    public void ensureFreshMetadata() {

    }

    public void close() {

    }

    public void poll(int timeout, Condition condition) {

    }

    public void awaitPendingRequests(Broker broker, int timeout) {

    }

    public int pendingRequestCount(Broker broker) {
        return 0;
    }

    public void disConnect(Broker broker) {

    }

    public void failUnsentRequests(Broker broker, RuntimeException e) {

    }

    public void tryConnect(Broker broker) {

    }

    public void connectionFailed(Broker broker) {

    }
}
