package com.vcvinci.remote;

import com.vcvinci.common.annotation.Nullable;
import com.vcvinci.common.cluster.Broker;
import com.vcvinci.protocol.request.ApiVersionRequest;
import com.vcvinci.protocol.request.Request;
import com.vcvinci.protocol.response.Response;
import com.vcvinci.remote.callback.RequestCompletionHandler;
import com.ucarinc.dove.util.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * 公用的远程连接客户端
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月5日 上午10:40:32
 */
public class CommonRemoteClient {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private RemoteClient client;
    private MetadataUpdater metadataUpdater = null;
    private Map<Integer, NodeConnectionState> connectionStates;
    private int requestTimeout;
    private int reconnectionInterval;
    private boolean getBrokerVersion;
    private Map<Integer, ApiVersionRequest> apiVersionRequests;
    private List<Response> abortedSends;
    private Map<String, Request> clientRequests;

    public CommonRemoteClient(RemoteClient client) {
        this.client = client;
    }

    public String availableHostOfRandom(List<Broker> brokers) {
        String addr = client.availableHostOfRandom();
        if(StringUtils.isNotBlank(addr)){
            return addr;
        }
        Broker broker = availableBrokerOrNull(brokers);
        if(broker == null){
            throw new RuntimeException("No host available");
        }
        return broker.addr();
    }

    public boolean isReady(Broker broker) throws Exception {
        checkBroker(broker);
        return client.connected(broker.addr());
    }

    public void ready(Broker broker) throws TimeoutException, InterruptedException {
        checkBroker(broker);
        client.connect(broker.addr());
    }

    public boolean canConnect(Broker broker) {
        checkBroker(broker);
        String host = broker.addr();
        if(client.connected(host)){
            return true;
        }
        try{
            client.connect(host);
            return client.connected(host);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (TimeoutException e) {
            logger.warn("Failed connect to host: {}", host);
        }
        return false;
    }

    public void connect(String host) throws Exception {
        // check
        client.connect(host);
    }

    public void start() throws Exception {
        client.start();
    }

    public void disconnect(Broker broker) {
        checkBroker(broker);
        client.removeChannel(broker.addr());
    }

    public void close() {
        client.shutdown();
    }

    public void connectionFailed(Broker broker) {

    }

    public boolean canSendRequest(Broker broker) {
        return false;
    }

    public void send(Request request, String addr, long timeoutMillis, RequestCompletionHandler callback) throws TimeoutException, InterruptedException {
        client.invokeAsync(addr, request, timeoutMillis, callback);
    }

    public Response send(Request request, String host, long timeoutMillis) throws TimeoutException, InterruptedException {
        return client.invokeSync(host, request, timeoutMillis);
    }

    public List<Response> poll(int timeout) {
        return null;
    }

    public void completeResponses(List<Response> responses) {

    }

    public int clientRequestCount(Broker broker) {
        return 0;
    }

    @Nullable
    public Broker availableBrokerOrNull(List<Broker> brokerList){

        if(CollectionUtil.isEmpty(brokerList)){
            return null;
        }
        List<Broker> brokers = new ArrayList<>();
        brokers.addAll(brokerList);
        Random random = new Random();
        Broker broker = brokers.get(random.nextInt(brokers.size()));
        while(!canConnect(broker)){
            brokers.remove(broker);
            if(brokers.size() == 0){
                return null;
            }
            broker = brokers.get(random.nextInt(brokers.size()));
        }
        return broker;
    }

    public Broker getLeastLoadedBroker() {
        return null;
    }

    public Response parseStruct(ByteBuffer buf, Request request) {
        return null;
    }

    public void processDisconnection(Broker broker) {

    }

    public void processTimeoutRequest() {

    }

    public void processAbortSends() {

    }

    public void processCompletedSends() {

    }

    public void processCompletedReceives() {

    }

    public void processApiVersionRespnse() {

    }

    public void processConnections() {

    }

    public void sendApiVersionsRequests() {

    }

    class MetadataUpdater {

    }

    private void checkBroker(Broker broker) {
        if (null == broker) {
            throw new RuntimeException("broker is null !");
        }
    }
}
