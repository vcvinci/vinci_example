package com.vcvinci.common.util.zookeeper;

import com.vcvinci.common.scheduler.DoveScheduler;
import com.vcvinci.common.util.zookeeper.exception.ZooKeeperClientAuthFailedException;
import com.vcvinci.common.util.zookeeper.exception.ZooKeeperClientExpiredException;
import com.vcvinci.common.util.zookeeper.exception.ZooKeeperClientTimeoutException;
import com.vcvinci.common.util.zookeeper.listener.ZkChildChangeListener;
import com.vcvinci.common.util.zookeeper.listener.ZkDataChangListener;
import com.vcvinci.common.util.zookeeper.listener.ZkStateChangeListener;
import com.vcvinci.common.util.zookeeper.request.AsyncRequest;
import com.vcvinci.common.util.zookeeper.request.CreateRequest;
import com.vcvinci.common.util.zookeeper.request.DeleteRequest;
import com.vcvinci.common.util.zookeeper.request.ExistsRequest;
import com.vcvinci.common.util.zookeeper.request.GetAclRequest;
import com.vcvinci.common.util.zookeeper.request.GetChildrenRequest;
import com.vcvinci.common.util.zookeeper.request.GetDataRequest;
import com.vcvinci.common.util.zookeeper.request.SetAclRequest;
import com.vcvinci.common.util.zookeeper.request.SetDataRequest;
import com.vcvinci.common.util.zookeeper.response.AsyncResponse;
import com.vcvinci.common.util.zookeeper.response.CreateResponse;
import com.vcvinci.common.util.zookeeper.response.DeleteResponse;
import com.vcvinci.common.util.zookeeper.response.ExistsResponse;
import com.vcvinci.common.util.zookeeper.response.GetAclResponse;
import com.vcvinci.common.util.zookeeper.response.GetChildrenResponse;
import com.vcvinci.common.util.zookeeper.response.GetDataResponse;
import com.vcvinci.common.util.zookeeper.response.SetAclResponse;
import com.vcvinci.common.util.zookeeper.response.SetDataResponse;
import com.ucarinc.dove.util.Time;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ZookeeperClient {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private String connectString;

    private int sessionTimeout;

    private Time time;

    private ReentrantReadWriteLock initializationLock = new ReentrantReadWriteLock();

    private ReentrantLock isConnectedOrExpiredLock = new ReentrantLock();

    private Condition isConnectedOrExpiredCondition = isConnectedOrExpiredLock.newCondition();

    private Map<String, ZkDataChangListener> zkDataChangeListeners = new ConcurrentHashMap();

    private Map<String, ZkChildChangeListener> zkChildChangeListeners = new ConcurrentHashMap();

    private Map<String, ZkStateChangeListener> zkStateChangeListeners = new ConcurrentHashMap();

    private Semaphore inFlightRequests;

    private DoveScheduler expiryScheduler = new DoveScheduler(1, "zk-session-expiry-handler");

    private ZooKeeper zooKeeper;

    public ZookeeperClient(String connectString, int sessionTimeout, int connectionTimeoutMs, int maxInFlightRequests, Time time) {
        this.connectString = connectString;
        this.sessionTimeout = sessionTimeout;
        this.time = time;
        this.inFlightRequests = new Semaphore(maxInFlightRequests);
        try {
            zooKeeper = new ZooKeeper(connectString, sessionTimeout, new ZooKeeperClientWatcher());
        } catch (IOException e) {
            LOGGER.error("初始化ZK出现异常，请检查zookeeper服务是否正常");
        }
        waitUntilConnected(connectionTimeoutMs, TimeUnit.MILLISECONDS);
    }

    public AsyncResponse retryRequestsUntilConnected(AsyncRequest request) {
        List requests = new ArrayList();
        requests.add(request);
        List<AsyncResponse> responses = retryRequestsUntilConnected(requests);
        return responses.get(0);
    }

    public List<AsyncResponse> retryRequestsUntilConnected(List<AsyncRequest> requests) {
        List<AsyncResponse> responses = new ArrayList<>();
        while (!requests.isEmpty()) {
            List<AsyncResponse> batchResponse = handleRequests(requests);
            List<AsyncRequest> lossRequest = new ArrayList<>();
            for (int i = 0; i < batchResponse.size(); i++) {
                //虽然是异步，但zookeeper会保证请求和响应的顺序一致，因此这里可以用response的下标get出对应的request
                AsyncResponse response = batchResponse.get(i);
                if (response.getResultCode() == KeeperException.Code.CONNECTIONLOSS) {
                    lossRequest.add(requests.get(i));
                } else {
                    responses.add(response);
                }
            }
            requests = lossRequest;
            if (!requests.isEmpty()) {
                waitUntilConnected();
            }
        }
        return responses;
    }

    public AsyncResponse handleRequest(AsyncRequest request) {
        List requests = new ArrayList();
        requests.add(request);
        List<AsyncResponse> queue = handleRequests(requests);
        return queue.get(0);
    }

    public List<AsyncResponse> handleRequests(List<AsyncRequest> requests) {
        if (CollectionUtils.isEmpty(requests)) {
            return new ArrayList<>(0);
        }

        final CountDownLatch countDownLatch = new CountDownLatch(requests.size());
        final List<AsyncResponse> responseList = new ArrayList<>(requests.size());

        for (AsyncRequest request : requests) {
            try {
                inFlightRequests.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            initializationLock.readLock().lock();
            try {
                send(request, responseList, countDownLatch);
            } finally {
                initializationLock.readLock().unlock();
                inFlightRequests.release();
            }
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error("线程被中断", e);
        }
        return responseList;
    }

    private void send(AsyncRequest request, final List<AsyncResponse> responseList, final CountDownLatch countDownLatch) {
        final long sendTimeMs = time.hiResClockMs();
        if (request instanceof CreateRequest) {
            CreateRequest createRequest = (CreateRequest) request;
            zooKeeper.create(createRequest.getPath(), createRequest.getData(), createRequest.getAcl(), createRequest.getCreateMode(), new AsyncCallback.StringCallback() {
                @Override
                public void processResult(int rc, String path, Object ctx, String name) {
                    AsyncResponse response = new CreateResponse(KeeperException.Code.get(rc), path, ctx, name, new ResponseMetadata(sendTimeMs, time.hiResClockMs()));
                    responseList.add(response);
                    countDownLatch.countDown();
                }
            }, createRequest.getCtx());
        } else if (request instanceof ExistsRequest) {
            ExistsRequest existsRequest = (ExistsRequest) request;
            zooKeeper.exists(existsRequest.getPath(), shoudWatch(request), new AsyncCallback.StatCallback() {
                @Override
                public void processResult(int rc, String path, Object ctx, Stat stat) {
                    AsyncResponse response = new ExistsResponse(KeeperException.Code.get(rc), path, ctx, stat, new ResponseMetadata(sendTimeMs, time.hiResClockMs()));
                    responseList.add(response);
                    countDownLatch.countDown();
                }
            }, request.getCtx());
        } else if (request instanceof GetDataRequest) {
            GetDataRequest getDataRequest = (GetDataRequest) request;
            zooKeeper.getData(getDataRequest.getPath(), shoudWatch(request), new AsyncCallback.DataCallback() {
                @Override
                public void processResult(int rc, String path, Object ctx, byte[] bytes, Stat stat) {
                    AsyncResponse response = new GetDataResponse(KeeperException.Code.get(rc), path, ctx, bytes, stat, new ResponseMetadata(sendTimeMs, time.hiResClockMs()));
                    responseList.add(response);
                    countDownLatch.countDown();
                }
            }, getDataRequest.getCtx());
        } else if (request instanceof GetChildrenRequest) {
            GetChildrenRequest getChildrenRequest = (GetChildrenRequest) request;
            zooKeeper.getChildren(getChildrenRequest.getPath(), shoudWatch(request), new AsyncCallback.Children2Callback() {
                @Override
                public void processResult(int rc, String path, Object ctx, List<String> childrens, Stat stat) {
                    AsyncResponse response = new GetChildrenResponse(KeeperException.Code.get(rc), path, ctx, childrens, stat, new ResponseMetadata(sendTimeMs, time.hiResClockMs()));
                    responseList.add(response);
                    countDownLatch.countDown();
                }
            }, getChildrenRequest.getCtx());
        } else if (request instanceof SetDataRequest) {
            SetDataRequest setDataRequest = (SetDataRequest) request;
            zooKeeper.setData(setDataRequest.getPath(), setDataRequest.getData(), setDataRequest.getVersion(), new AsyncCallback.StatCallback() {
                @Override
                public void processResult(int rc, String path, Object ctx, Stat stat) {
                    AsyncResponse response = new SetDataResponse(KeeperException.Code.get(rc), path, ctx, stat, new ResponseMetadata(sendTimeMs, time.hiResClockMs()));
                    responseList.add(response);
                    countDownLatch.countDown();
                }
            }, setDataRequest.getCtx());
        } else if (request instanceof DeleteRequest) {
            DeleteRequest deleteRequest = (DeleteRequest) request;
            zooKeeper.delete(deleteRequest.getPath(), deleteRequest.getVersion(), new AsyncCallback.VoidCallback() {
                @Override
                public void processResult(int rc, String path, Object ctx) {
                    AsyncResponse response = new DeleteResponse(KeeperException.Code.get(rc), path, ctx, new ResponseMetadata(sendTimeMs, time.hiResClockMs()));
                    responseList.add(response);
                    countDownLatch.countDown();
                }
            }, deleteRequest.getCtx());
        } else if (request instanceof GetAclRequest) {
            GetAclRequest getAclRequest = (GetAclRequest) request;
            zooKeeper.getACL(getAclRequest.getPath(), null, new AsyncCallback.ACLCallback() {
                @Override
                public void processResult(int rc, String path, Object ctx, List<ACL> acl, Stat stat) {
                    GetAclResponse response = new GetAclResponse(KeeperException.Code.get(rc), path, ctx, acl, stat, new ResponseMetadata(sendTimeMs, time.hiResClockMs()));
                    responseList.add(response);
                    countDownLatch.countDown();
                }
            }, getAclRequest.getCtx());
        } else if (request instanceof SetAclRequest) {
            SetAclRequest setAclRequest = (SetAclRequest) request;
            zooKeeper.setACL(setAclRequest.getPath(), setAclRequest.getAcl(), setAclRequest.getVersion(), new AsyncCallback.StatCallback() {
                @Override
                public void processResult(int rc, String path, Object ctx, Stat stat) {
                    AsyncResponse response = new SetAclResponse(KeeperException.Code.get(rc), path, ctx, stat, new ResponseMetadata(sendTimeMs, time.hiResClockMs()));
                    responseList.add(response);
                    countDownLatch.countDown();
                }
            }, setAclRequest.getCtx());
        }
    }

    private boolean shoudWatch(AsyncRequest request) {
        if (request instanceof GetChildrenRequest) {
            return zkChildChangeListeners.containsKey(request.getPath());
        } else if (request instanceof ExistsRequest || request instanceof GetDataRequest) {
            return zkDataChangeListeners.containsKey(request.getPath());
        } else {
            return false;
        }

    }

    private class ZooKeeperClientWatcher implements Watcher {

        @Override
        public void process(WatchedEvent event) {
            if (event.getPath() == null) {
                Event.KeeperState state = event.getState();
                isConnectedOrExpiredLock.lock();
                isConnectedOrExpiredCondition.signalAll();
                isConnectedOrExpiredLock.unlock();

                if (state == Event.KeeperState.Expired) {
                    scheduleSessionExpiryHandler();
                } else if (state == Event.KeeperState.AuthFailed) {
                    for (Map.Entry<String, ZkStateChangeListener> entry : zkStateChangeListeners.entrySet()) {
                        entry.getValue().onAuthFailure();
                    }
                }
            } else {
                String path = event.getPath();
                Event.EventType type = event.getType();
                if (type == Event.EventType.NodeChildrenChanged) {
                    ZkChildChangeListener zkChildChangeListener = zkChildChangeListeners.get(path);
                    if (zkChildChangeListener != null) {
                        zkChildChangeListener.handleChildChange();
                    }
                } else if (type == Event.EventType.NodeDataChanged) {
                    ZkDataChangListener zkDataChangListener = zkDataChangeListeners.get(path);
                    if (zkDataChangListener != null) {
                        zkDataChangListener.handleDataChange();
                    }
                } else if (type == Event.EventType.NodeCreated) {
                    ZkDataChangListener zkDataChangListener = zkDataChangeListeners.get(path);
                    if (zkDataChangListener != null) {
                        zkDataChangListener.handleCreation();
                    }
                } else if (type == Event.EventType.NodeDeleted) {
                    ZkDataChangListener zkDataChangListener = zkDataChangeListeners.get(path);
                    if (zkDataChangListener != null) {
                        zkDataChangListener.handleDeletion();
                    }
                }
            }
        }
    }

    public void registerZkChildChangeListener(ZkChildChangeListener zkChildChangeListener) {
        zkChildChangeListeners.put(zkChildChangeListener.path(), zkChildChangeListener);
    }

    public void unRegisterZkChildChangeListener(ZkChildChangeListener zkChildChangeListener) {
        zkChildChangeListeners.remove(zkChildChangeListener.path());
    }

    public void registerZkDataChangeListener(ZkDataChangListener zkDataChangListener) {
        zkDataChangeListeners.put(zkDataChangListener.path(), zkDataChangListener);
    }

    public void unRegisterZkDataChangeListener(ZkDataChangListener zkDataChangListener) {
        zkDataChangeListeners.remove(zkDataChangListener.path());
    }

    public void registerStateChangeListener(ZkStateChangeListener zkStateChangeListener) {
        zkStateChangeListeners.put(zkStateChangeListener.getName(), zkStateChangeListener);
    }

    public void unRegisterStateChangeListener(ZkStateChangeListener zkStateChangeListener) {
        zkStateChangeListeners.remove(zkStateChangeListener.getName());
    }

    private void reinitialize() {
        for (Map.Entry<String, ZkStateChangeListener> entry : zkStateChangeListeners.entrySet()) {
            entry.getValue().beforeInitializingSession();
        }

        initializationLock.writeLock().lock();
        if (!zooKeeper.getState().isAlive()) {
            try {
                zooKeeper.close();
                boolean connected = false;
                while (!connected) {
                    zooKeeper = new ZooKeeper(connectString, sessionTimeout, new ZooKeeperClientWatcher());
                    connected = true;
                }
            } catch (Exception e) {
                LOGGER.error("Error when recreating ZooKeeper, retrying after a short sleep", e);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    LOGGER.error("线程被中断", e1);
                }
            }
        }
    }

    private void forceReinitialize() {
        try {
            zooKeeper.close();
            reinitialize();
        } catch (InterruptedException e) {
            LOGGER.error("zookeeper关闭异常", e);
        }
    }

    private void scheduleSessionExpiryHandler() {
        expiryScheduler.scheduleOnce("zk session过期清理任务", new Runnable() {
            @Override
            public void run() {
                reinitialize();
            }
        });
    }

    public void waitUntilConnected() {
        waitUntilConnected(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    private void waitUntilConnected(long timeout, TimeUnit timeUnit) {
        long nanos = timeUnit.toNanos(timeout);
        ZooKeeper.States state = zooKeeper.getState();
        while (!state.isConnected() && state.isAlive()) {
            if (nanos <= 0) {
                throw new ZooKeeperClientTimeoutException("Timed out waiting for connection while in state: " + state);
            }
            isConnectedOrExpiredLock.lock();
            try {
                nanos = isConnectedOrExpiredCondition.awaitNanos(nanos);
                state = zooKeeper.getState();
            } catch (InterruptedException e) {
                LOGGER.error("线程被中断", e);
            } finally {
                isConnectedOrExpiredLock.unlock();
            }
        }

        if (state == ZooKeeper.States.AUTH_FAILED) {
            throw new ZooKeeperClientAuthFailedException("Auth failed either before or while waiting for connection");
        }

        if (state == ZooKeeper.States.CLOSED) {
            throw new ZooKeeperClientExpiredException("Session expired either before or while waiting for connection");
        }
    }

    public void close() {
        try {
            zooKeeper.close();
        } catch (InterruptedException e) {
            LOGGER.error("关闭zk出现异常", e);
        }
    }
}

