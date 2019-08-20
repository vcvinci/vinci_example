package com.vcvinci.common.util.zookeeper;

import com.vcvinci.common.cluster.Broker;
import com.vcvinci.common.cluster.BrokerInfo;
import com.vcvinci.common.partition.TopicPartition;
import com.vcvinci.common.sr.LeaderSrAndControllerEpoch;
import com.vcvinci.common.util.zookeeper.listener.ZkChildChangeListener;
import com.vcvinci.common.util.zookeeper.listener.ZkDataChangListener;
import com.vcvinci.common.util.zookeeper.listener.ZkStateChangeListener;
import com.vcvinci.common.util.zookeeper.request.AsyncRequest;
import com.vcvinci.common.util.zookeeper.request.CreateRequest;
import com.vcvinci.common.util.zookeeper.request.DeleteRequest;
import com.vcvinci.common.util.zookeeper.request.ExistsRequest;
import com.vcvinci.common.util.zookeeper.request.GetChildrenRequest;
import com.vcvinci.common.util.zookeeper.request.GetDataRequest;
import com.vcvinci.common.util.zookeeper.request.SetDataRequest;
import com.vcvinci.common.util.zookeeper.response.AsyncResponse;
import com.vcvinci.common.util.zookeeper.response.CreateResponse;
import com.vcvinci.common.util.zookeeper.response.DeleteResponse;
import com.vcvinci.common.util.zookeeper.response.ExistsResponse;
import com.vcvinci.common.util.zookeeper.response.GetChildrenResponse;
import com.vcvinci.common.util.zookeeper.response.GetDataResponse;
import com.ucarinc.dove.util.CollectionUtil;
import com.ucarinc.dove.util.Time;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月2日 下午8:32:10
 * @description 类说明
 */
public class DoveZkUtil {

    private Logger LOGGER = LoggerFactory.getLogger(DoveZkUtil.class);

    private ZookeeperClient zookeeperClient;

    public DoveZkUtil(String connectString, int sessionTimeout, int connectionTimeoutMs, int maxInFlightRequests, Time time) {
        zookeeperClient = new ZookeeperClient(connectString, sessionTimeout, connectionTimeoutMs, maxInFlightRequests, time);
    }

    public void registerStateChangeListener(ZkStateChangeListener zkStateChangeListener) {
        zookeeperClient.registerStateChangeListener(zkStateChangeListener);
    }

    public void unRegisterStateChangeListener(ZkStateChangeListener zkStateChangeListener) {
        zookeeperClient.unRegisterStateChangeListener(zkStateChangeListener);
    }

    public void registerZkDataChangeListener(ZkDataChangListener zkDataChangListener) {
        zookeeperClient.registerZkDataChangeListener(zkDataChangListener);
    }

    public void unRegisterZkDataChangeListener(ZkDataChangListener zkDataChangListener) {
        zookeeperClient.unRegisterZkDataChangeListener(zkDataChangListener);
    }

    public void registerZkChildChangeListener(ZkChildChangeListener zkChildChangeListener) {
        zookeeperClient.registerZkChildChangeListener(zkChildChangeListener);
    }

    public void unRegisterZkChildChangeListener(ZkChildChangeListener zkChildChangeListener) {
        zookeeperClient.unRegisterZkChildChangeListener(zkChildChangeListener);
    }

    public boolean registerZkDataChangeListenerAndCheckExistece(ZkDataChangListener zkDataChangListener) {
        zookeeperClient.registerZkDataChangeListener(zkDataChangListener);
        ExistsRequest existsRequest = new ExistsRequest(zkDataChangListener.path(), null);
        AsyncResponse response = zookeeperClient.retryRequestsUntilConnected(existsRequest);
        if (response.getResultCode() == KeeperException.Code.OK) {
            return true;
        } else {
            return false;
        }
    }

    public boolean registerZkChildChangeListenerAndCheckExistece(ZkChildChangeListener zkChildChangeListener) {
        zookeeperClient.registerZkChildChangeListener(zkChildChangeListener);
        ExistsRequest existsRequest = new ExistsRequest(zkChildChangeListener.path(), null);
        AsyncResponse response = zookeeperClient.retryRequestsUntilConnected(existsRequest);
        if (response.getResultCode() == KeeperException.Code.OK) {
            return true;
        } else {
            return false;
        }
    }

    public void makeSurePathExists(List<String> paths) {
        for (String path : paths) {
            if (!pathExists(path)) {
                try {
                    createPersistentPath(path, null);
                } catch (KeeperException e) {
                    LOGGER.error("创建持久化节点时出错：", e);
                }
            }
        }
    }

    public boolean pathExists(String path) {
        ExistsRequest existsRequest = new ExistsRequest(path, null);
        ExistsResponse existsResponse = (ExistsResponse) zookeeperClient.retryRequestsUntilConnected(existsRequest);
        if (existsResponse.getResultCode() == KeeperException.Code.OK) {
            return true;
        } else {
            return false;
        }
    }

    public String getData(String path) throws KeeperException {
        GetDataRequest getDataRequest = new GetDataRequest(path);
        AsyncResponse asyncResponse = zookeeperClient.retryRequestsUntilConnected(getDataRequest);
        if (asyncResponse.getResultCode() != KeeperException.Code.OK) {
            throw KeeperException.create(asyncResponse.getResultCode());
        } else {
            return new String(((GetDataResponse) asyncResponse).getData());
        }

    }

    public List<String> getChildrens(String path) throws KeeperException {
        GetChildrenRequest getChildrenRequest = new GetChildrenRequest(path, null);
        AsyncResponse asyncResponse = zookeeperClient.retryRequestsUntilConnected(getChildrenRequest);
        if (asyncResponse.getResultCode() != KeeperException.Code.OK) {
            throw KeeperException.create(asyncResponse.getResultCode());
        } else {
            return ((GetChildrenResponse) asyncResponse).getChildren();
        }
    }

    public void createEphemeralPath(String path, byte[] data) throws KeeperException {
        CreateRequest createRequest = new CreateRequest(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        CreateResponse createResponse = (CreateResponse) zookeeperClient.retryRequestsUntilConnected(createRequest);
        if (createResponse.getResultCode() != KeeperException.Code.OK) {
            throw KeeperException.create(createResponse.getResultCode());
        }
    }

    public void createPersistentPath(String path, byte[] data) throws KeeperException {
        CreateRequest createRequest = new CreateRequest(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        CreateResponse createResponse = (CreateResponse) zookeeperClient.retryRequestsUntilConnected(createRequest);
        if (createResponse.getResultCode() != KeeperException.Code.OK) {
            throw KeeperException.create(createResponse.getResultCode());
        }
    }

    public void createPersistentPathAndParent(String path, byte[] data) throws KeeperException {
        CreateRequest createRequest = new CreateRequest(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        CreateResponse createResponse = (CreateResponse) zookeeperClient.retryRequestsUntilConnected(createRequest);
        if (createResponse.getResultCode() == KeeperException.Code.NODEEXISTS) {
            throw KeeperException.create(createResponse.getResultCode());
        } else if (createResponse.getResultCode() == KeeperException.Code.NONODE) {
            String parentPath = path.substring(0, path.lastIndexOf("/"));
            createPersistentPath(parentPath, null);
        }
    }

    public String createSequentialPersistentPath(String path, byte[] data) throws KeeperException {
        CreateRequest createRequest = new CreateRequest(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        CreateResponse createResponse = (CreateResponse) zookeeperClient.retryRequestsUntilConnected(createRequest);
        if (createResponse.getResultCode() != KeeperException.Code.OK) {
            throw KeeperException.create(createResponse.getResultCode());
        }
        return createResponse.getName();
    }

    public String createSequentialEphemeralPath(String path, byte[] data) throws KeeperException {
        CreateRequest createRequest = new CreateRequest(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        CreateResponse createResponse = (CreateResponse) zookeeperClient.retryRequestsUntilConnected(createRequest);
        if (createResponse.getResultCode() != KeeperException.Code.OK) {
            throw KeeperException.create(createResponse.getResultCode());
        }
        return createResponse.getName();
    }

    public void updatePath(String path, byte[] data) throws KeeperException {
        SetDataRequest setDataRequest = new SetDataRequest(path, null, data, -1);
        AsyncResponse asyncResponse = zookeeperClient.retryRequestsUntilConnected(setDataRequest);
        if (asyncResponse.getResultCode() != KeeperException.Code.OK) {
            throw KeeperException.create(asyncResponse.getResultCode());
        }
    }

    public void createOrUpdatePersistentPath(String path, byte[] data) {
        try {
            if (pathExists(path)) {
                updatePath(path, data);
            } else {
                createPersistentPathAndParent(path, data);
            }
        } catch (KeeperException e) {
            LOGGER.error("创建或更新zk节点出现异常，path:{}", path, e);
        }
    }

    public void deletePath(String path) throws KeeperException {
        if (pathExists(path)) {
            DeleteRequest deleteRequest = new DeleteRequest(path, null, -1);
            AsyncResponse asyncResponse = zookeeperClient.retryRequestsUntilConnected(deleteRequest);
            if (asyncResponse.getResultCode() != KeeperException.Code.OK) {
                throw KeeperException.create(asyncResponse.getResultCode());
            }
        }
    }

    public void deleteChild(String path) {
        try {
            if (pathExists(path)) {
                List<String> childrens = getChildrens(path);
                if (CollectionUtil.isEmpty(childrens)) {
                    return;
                }
                List<AsyncRequest> deleteRequests = new ArrayList<>();
                for (String children : childrens) {
                    DeleteRequest deleteRequest = new DeleteRequest(path + "/" + children, null, -1);
                    deleteRequests.add(deleteRequest);
                }
                List<AsyncResponse> asyncResponses = zookeeperClient.retryRequestsUntilConnected(deleteRequests);
                for (AsyncResponse asyncRespons : asyncResponses) {
                    DeleteResponse deleteResponse = (DeleteResponse) asyncRespons;
                    if (deleteResponse.getResultCode() != KeeperException.Code.OK) {
                        throw KeeperException.create(deleteResponse.getResultCode());
                    }
                }
            }
        } catch (KeeperException e) {
            LOGGER.error("删除路径：{}下的子节点出现异常:", path, e);
        }
    }

    public void deletePathAndChild(String path) {
        try {
            if (path.equals("/zookeeper")) {
                return;
            }
            List<String> childrens = getChildrens(path);
            if (!CollectionUtil.isEmpty(childrens)) {
                for (String children : childrens) {
                    if (path.equals("/")) {
                        deletePathAndChild("/" + children);
                    } else {
                        deletePathAndChild(path + "/" + children);
                    }
                }
                if (!path.equals("/")) {
                    LOGGER.debug("删除path:{}", path);
                    deletePath(path);
                }
            } else {
                if (!path.equals("/")) {
                    LOGGER.debug("删除path:{}", path);
                    deletePath(path);
                }
            }
        } catch (KeeperException e) {
            LOGGER.info("递归删除节点出现异常：path:{},", e.getPath(), e);
        }
    }

    public int getControllerId() {
        GetDataRequest getDataRequest = new GetDataRequest(ZkData.ControllerZNode.getPath());
        GetDataResponse getDataResponse = (GetDataResponse) zookeeperClient.retryRequestsUntilConnected(getDataRequest);
        if (getDataResponse.getResultCode() == KeeperException.Code.OK) {
            return ZkData.ControllerZNode.decode(getDataResponse.getData());
        } else {
            return -1;
        }
    }

    public int getControllerEpoch() {
        GetDataRequest getDataRequest = new GetDataRequest(ZkData.ControllerEpochZNode.getPath());
        GetDataResponse getDataResponse = (GetDataResponse) zookeeperClient.retryRequestsUntilConnected(getDataRequest);
        if (getDataResponse.getResultCode() == KeeperException.Code.OK) {
            return ZkData.ControllerEpochZNode.decode(getDataResponse.getData());
        } else {
            return 0;
        }
    }

    public int getPartitionCount(String topic) {
        List<String> partitions;
        try {
            partitions = getChildrens(ZkData.TopicPartitionsZNode.getPath(topic));
            return partitions.size();
        } catch (KeeperException e) {
            return -1;
        }
    }

    public String getOffsetMetadataFromZk(String groupName) {
        try {
            return getData(ZkData.ConsumerGroupNode.getPathForGroupName(groupName));
        } catch (KeeperException e) {
            LOGGER.error("从zk获取offset失败，路径{}", ZkData.ConsumerGroupNode.getPathForGroupName(groupName), e);
            return null;
        }
    }

    public long getCheckPoint(int partition) {
        //如果当前路径不存在, 可能是zk上还没有存储offset信息, 则从0开始加载
        try {
            return Integer.parseInt(getData(ZkData.CheckPointZNode.getPath(partition)));
        } catch (KeeperException e) {
            return 0;
        }
    }

    public Set<Broker> getAllBrokersFromZk() {
        Set<Broker> brokersInZk = new HashSet<>();
        List<String> idsStr;
        try {
            idsStr = getChildrens(ZkData.BrokerIdsZNode.getPath());
        } catch (KeeperException e) {
            return new HashSet<>();
        }

        List<AsyncRequest> getBrokerInfoRequests = new ArrayList<>();
        for (String idStr : idsStr) {
            getBrokerInfoRequests.add(new GetDataRequest(ZkData.BrokerIdZNode.getPath(Integer.parseInt(idStr)), idStr));
        }

        List<AsyncResponse> asyncResponses = zookeeperClient.retryRequestsUntilConnected(getBrokerInfoRequests);
        for (AsyncResponse asyncRespons : asyncResponses) {
            GetDataResponse brokerInfoResponse = (GetDataResponse) asyncRespons;
            Broker broker = ZkData.BrokerIdZNode.decode(Integer.parseInt((String) brokerInfoResponse.getCtx()), brokerInfoResponse.getData()).getBroker();
            brokersInZk.add(broker);
        }
        return brokersInZk;
    }

    public Set<String> getAllTopicFromZk() {
        try {
            List<String> topicsStr = getChildrens(ZkData.TopicsZNode.getPath());
            return new HashSet<>(topicsStr);
        } catch (KeeperException e) {
            return new HashSet<>();
        }
    }

    public Set<String> getAllGroupFromZk() {
        try {
            return new HashSet<>(getChildrens(ZkData.ConsumerGroupNode.getPath()));
        } catch (KeeperException e) {
            return new HashSet<>();
        }
    }

    public LeaderSrAndControllerEpoch getPartition2LeaderInfoFromZk(TopicPartition topicPartition) {
        try {
            String data = getData(ZkData.TopicPartitionStateZNode.getPath(topicPartition));
            LeaderSrAndControllerEpoch leaderSrAndControllerEpoch = ZkData.TopicPartitionStateZNode.decode(data.getBytes());
            return leaderSrAndControllerEpoch;
        } catch (KeeperException e) {
            LOGGER.error("获取分区{}的state失败：", topicPartition, e);
            return null;
        }
    }

    public Map<TopicPartition, LeaderSrAndControllerEpoch> getPartition2LeaderInfoFromZk(Set<TopicPartition> topicPartitions) {
        Map<TopicPartition, LeaderSrAndControllerEpoch> result = new HashMap();
        List<AsyncRequest> getPartitionStateRequest = new ArrayList<>();
        for (TopicPartition topicPartition : topicPartitions) {
            getPartitionStateRequest.add(new GetDataRequest(ZkData.TopicPartitionStateZNode.getPath(topicPartition), topicPartition));
        }
        List<AsyncResponse> asyncResponses = zookeeperClient.retryRequestsUntilConnected(getPartitionStateRequest);
        for (AsyncResponse asyncRespons : asyncResponses) {
            GetDataResponse getDataResponse = (GetDataResponse) asyncRespons;
            if (getDataResponse.getResultCode() == KeeperException.Code.OK) {
                LeaderSrAndControllerEpoch leaderSrAndControllerEpoch = ZkData.TopicPartitionStateZNode.decode(getDataResponse.getData());
                result.put((TopicPartition) getDataResponse.getCtx(), leaderSrAndControllerEpoch);
            }
        }
        return result;
    }

    public Map<TopicPartition, List<Integer>> getPartition2Replicas(Set<String> allTopics) {
        Map<TopicPartition, List<Integer>> result = new HashMap<>();
        List<AsyncRequest> getTopicInfoRequests = new ArrayList<>();
        for (String topic : allTopics) {
            getTopicInfoRequests.add(new GetDataRequest(ZkData.TopicZNode.getPath(topic), topic));
        }
        List<AsyncResponse> asyncResponses = zookeeperClient.retryRequestsUntilConnected(getTopicInfoRequests);
        for (AsyncResponse asyncRespons : asyncResponses) {
            GetDataResponse getDataResponse = (GetDataResponse) asyncRespons;
            if (getDataResponse.getResultCode() == KeeperException.Code.OK) {
                result.putAll(ZkData.TopicZNode.decode(getDataResponse.getCtx().toString(), getDataResponse.getData()));
            }
        }
        return result;
    }

    public Set<TopicPartition> getSrChangePartitions() {
        Set<TopicPartition> tps = new HashSet<>();
        List<String> childrens;
        try {
            childrens = getChildrens(ZkData.SrChangeNotificationZNode.getPath());
        } catch (KeeperException e) {
            return new HashSet<>();
        }
        List<AsyncRequest> getDataRequests = new ArrayList<>();
        for (String children : childrens) {
            GetDataRequest getDataRequest = new GetDataRequest(ZkData.SrChangeNotificationZNode.getPath() + "/" + children);
            getDataRequests.add(getDataRequest);
        }
        List<AsyncResponse> asyncResponses = zookeeperClient.retryRequestsUntilConnected(getDataRequests);
        for (AsyncResponse asyncResponse : asyncResponses) {
            GetDataResponse getDataResponse = (GetDataResponse) asyncResponse;
            if (getDataResponse.getResultCode() == KeeperException.Code.OK) {
                Set<TopicPartition> topicPartitions = ZkData.SrChangeNotificationChildZNode.decode(getDataResponse.getData());
                tps.addAll(topicPartitions);
            }
        }
        List<AsyncRequest> deleteRequests = new ArrayList<>();
        for (String children : childrens) {
            DeleteRequest deleteRequest = new DeleteRequest(ZkData.SrChangeNotificationChildZNode.getPath(children), null, -1);
            deleteRequests.add(deleteRequest);
        }
        zookeeperClient.retryRequestsUntilConnected(deleteRequests);
        return tps;
    }

    public Map<TopicPartition, List<Integer>> getPartitionReassignment() {
        Map<TopicPartition, List<Integer>> reassignPartitions = new HashMap<>();
        List<String> childrens;
        try {
            childrens = getChildrens(ZkData.ReassignPartitionsZNode.getPath());
        } catch (KeeperException e) {
            return new HashMap<>();
        }
        List<AsyncRequest> getDataRequests = new ArrayList<>();
        for (String children : childrens) {
            GetDataRequest getDataRequest = new GetDataRequest(ZkData.ReassignPartitionsZNode.getPath() + "/" + children);
            getDataRequests.add(getDataRequest);
        }
        List<AsyncResponse> asyncResponses = zookeeperClient.retryRequestsUntilConnected(getDataRequests);
        for (AsyncResponse asyncResponse : asyncResponses) {
            GetDataResponse getDataResponse = (GetDataResponse) asyncResponse;
            if (getDataResponse.getResultCode() == KeeperException.Code.OK) {
                Map<TopicPartition, List<Integer>> reassign = ZkData.ReassignPartitionsChildZNode.decode(getDataResponse.getData());
                reassignPartitions.putAll(reassign);
            }
        }
        List<AsyncRequest> deleteRequests = new ArrayList<>();
        for (String children : childrens) {
            DeleteRequest deleteRequest = new DeleteRequest(ZkData.ReassignPartitionsZNode.getPath() + "/" + children, null, -1);
            deleteRequests.add(deleteRequest);
        }
        zookeeperClient.retryRequestsUntilConnected(deleteRequests);
        return reassignPartitions;
    }

    public Set<String> getTopicTobeDelete() {
        try {
            List<String> topics = getChildrens(ZkData.DeleteTopicsZNode.getPath());
            return new HashSet<>(topics);
        } catch (KeeperException e) {
            return new HashSet<>();
        }
    }

    public Set<TopicPartition> getPreferredReplicaTopicPartitions() {
        try {
            return ZkData.PreferredReplicaElectionZNode.decode(getData(ZkData.PreferredReplicaElectionZNode.getPath()).getBytes());
        } catch (KeeperException e) {
            return new HashSet<>();
        }
    }

    public List<Integer> getShutdownBrokerIds() {
        try {
            List<Integer> shutdownBrokerIds = new ArrayList<>();
            List<String> childrens = getChildrens(ZkData.BrokerShutdownZNode.getPath());
            for (String children : childrens) {
                shutdownBrokerIds.add(Integer.parseInt(children));
            }
            return shutdownBrokerIds;
        } catch (KeeperException e) {
            return new ArrayList<>();
        }
    }

    public Set<TopicPartition> getAllTopicPartition() {
        Set<String> allTopicFromZk = this.getAllTopicFromZk();
        return this.getPartition2Replicas(allTopicFromZk).keySet();
    }

    public void createBrokerNodeInZk(BrokerInfo brokerInfo) throws KeeperException {
        createEphemeralPath(brokerInfo.getPath(), ZkData.BrokerIdZNode.encode(brokerInfo));
    }

    public void createControllerEpoch(int epoch) throws KeeperException {
        createPersistentPath(ZkData.ControllerEpochZNode.getPath(), String.valueOf(epoch).getBytes());
    }

    public void createTopicPartitionStatePath(TopicPartition topicPartition, LeaderSrAndControllerEpoch leaderSrAndControllerEpoch) throws KeeperException {
        if (!pathExists(ZkData.TopicPartitionsZNode.getPath(topicPartition.getTopic()))) {
            createPersistentPath(ZkData.TopicPartitionsZNode.getPath(topicPartition.getTopic()), null);
        }
        if (!pathExists(ZkData.TopicPartitionZNode.getPath(topicPartition))) {
            createPersistentPath(ZkData.TopicPartitionZNode.getPath(topicPartition), null);
        }

        String path = ZkData.TopicPartitionStateZNode.getPath(topicPartition);

        if (!pathExists(path)) {
            byte[] data = ZkData.TopicPartitionStateZNode.encode(leaderSrAndControllerEpoch);
            createPersistentPath(path, data);
        } else {
            throw new KeeperException.NodeExistsException();
        }
    }

    public void createSrChangeNotification(Set<TopicPartition> topicPartitions) throws KeeperException {
        createSequentialPersistentPath(ZkData.SrChangeNotificationZNode.getPath() + "/sr_change_", ZkData.SrChangeNotificationChildZNode.encode(topicPartitions));
    }

    public void createTopicDeletionPath(String topic) throws KeeperException {
        if (pathExists(ZkData.DeleteTopicsZNode.getPath() + "/" + topic)) {
            throw new KeeperException.NodeExistsException();
        }
        createPersistentPath(ZkData.DeleteTopicsZNode.getPath() + "/" + topic, null);
    }

    public void createPartitionReassignPath(Map<TopicPartition, List<Integer>> reassignment){
        try {
            createPersistentPath(ZkData.ReassignPartitionsChildZNode.getPath("reassign_partition_"), ZkData.ReassignPartitionsChildZNode.encode(reassignment));
        } catch (KeeperException e) {
            LOGGER.info("创建副本重新分配路径出现异常：", e);
        }
    }

    public void updateControllerEpoch(int newControllerEpoch) throws KeeperException {
        updatePath(ZkData.ControllerEpochZNode.getPath(), String.valueOf(newControllerEpoch).getBytes());
    }

    public void updatePartitionState(TopicPartition topicPartition, LeaderSrAndControllerEpoch leaderSrAndControllerEpoch) throws KeeperException {
        updatePath(ZkData.TopicPartitionStateZNode.getPath(topicPartition), ZkData.TopicPartitionStateZNode.encode(leaderSrAndControllerEpoch));
    }

    public void updateTopicData(String topic, Map<TopicPartition, List<Integer>> replicas) throws KeeperException {
        updatePath(ZkData.TopicZNode.getPath(topic), ZkData.TopicZNode.encode(replicas));
    }

    /**
     * 以group为维度存储每个group下面所有消费者的offset元数据
     * 注: 该元数据并不是最新的,甚至可能会落后很多, 仅作为一个历史记录
     *
     * @param groupName
     * @param jsonMetadata
     */
    public void createOrUpdateOffsetMetadata(String groupName, String jsonMetadata) {
        createOrUpdatePersistentPath(ZkData.ConsumerGroupNode.getPathForGroupName(groupName), jsonMetadata.getBytes());
    }

    /**
     * 存储内部topic每个分区的hw
     *
     * @param partition
     * @param offset
     */
    public void createOrUpdateCheckPoint(int partition, long offset) {
        createOrUpdatePersistentPath(ZkData.CheckPointZNode.getPath(partition), String.valueOf(offset).getBytes());
    }

    public void deleteTopicZNode(String topic) {
        deletePathAndChild(ZkData.TopicZNode.getPath(topic));
    }

    public void deleteTopicDeletion(String topic){
        deletePathAndChild(ZkData.DeleteTopicsZNode.getPath() + "/" + topic);
    }

    public void deleteAllShutdownBroker() {
        try {
            List<String> childrens = getChildrens(ZkData.BrokerShutdownZNode.getPath());
            for (String children : childrens) {
                deletePath(ZkData.BrokerShutdownZNode.getPath() + "/" + children);
            }
        } catch (KeeperException e) {
            LOGGER.error("删除shutdownBroker出现异常", e);
        }
    }

    public void deleteShutdownBroker(int brokerId){
        deletePathAndChild(ZkData.BrokerShutdownZNode.getPath() + "/" + brokerId);
    }

    public void deleteSrChangeNotifications() {
        try {
            List<String> childrens = getChildrens(ZkData.SrChangeNotificationZNode.getPath());
            List<AsyncRequest> requests = new ArrayList<>();
            for (String children : childrens) {
                DeleteRequest deleteRequest = new DeleteRequest(ZkData.SrChangeNotificationChildZNode.getPath(children), null, -1);
                requests.add(deleteRequest);
            }
            zookeeperClient.retryRequestsUntilConnected(requests);
        } catch (KeeperException e) {
            LOGGER.info("删除srChangeNotifications出现异常", e);
        }
    }

    public void deleteTopicDeletions() {
        try {
            List<String> childrens = getChildrens(ZkData.DeleteTopicsZNode.getPath());
            List<AsyncRequest> requests = new ArrayList<>();
            for (String children : childrens) {
                DeleteRequest deleteRequest = new DeleteRequest(ZkData.DeleteTopicsZNode.getPath() + "/" + children, null, -1);
                requests.add(deleteRequest);
            }
            zookeeperClient.retryRequestsUntilConnected(requests);
        } catch (KeeperException e) {
            LOGGER.info("删除TopicDeletions出现异常", e);
        }
    }

    public void deleteReassignPartitionsData() {
        if (pathExists(ZkData.ReassignPartitionsZNode.getPath())) {
            deleteChild(ZkData.ReassignPartitionsZNode.getPath());
        }
    }

    public void close() {
        zookeeperClient.close();
    }

    /**
     * 功能描述: <br>
     * 〈修改配置变化节点〉
     *
     * @param sanitizedEntityPath
     * @author HuangTaiHong
     * @date 2018.12.17 10:53:02
     */
    public void createConfigChangeNotification(String sanitizedEntityPath) throws KeeperException {
        createSequentialPersistentPath(ZkData.ConfigEntityChangeNotificationSequenceZNode.getPath(), ZkData.ConfigEntityChangeNotificationSequenceZNode.encode(sanitizedEntityPath));
    }

    /**
     * 功能描述: <br>
     * 〈根据配置类型与名称获取配置〉
     *
     * @param rootEntityType
     * @param sanitizedEntityName
     * @return > java.util.Properties
     * @throws KeeperException
     * @author HuangTaiHong
     * @date 2018.12.17 13:45:35
     */
    public Properties getEntityConfigs(String rootEntityType, String sanitizedEntityName) throws KeeperException {
        return ZkData.ConfigEntityZNode.decode(getData(ZkData.ConfigEntityZNode.getPath(rootEntityType, sanitizedEntityName)));
    }
}
