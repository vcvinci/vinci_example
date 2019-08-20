package com.vcvinci.common.partition;

import java.util.List;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月3日 上午7:30:13
 * @description 分区状态类
 * <p>
 * 分区状态实例，包含了分区的所有相关概念信息
 */
public class PartitionState {

    private int leaderEpoch;
    private int controllerEpoch;
    private List<Integer> srReplicas;
    private List<Integer> replicas;
    private int leaderId;
    private List<Integer> offlineReplicas;
    private boolean isNew;

    public PartitionState() {
    }

    public PartitionState(int leaderEpoch, int controllerEpoch, List<Integer> srReplicas, List<Integer> replicas, int leaderId, List<Integer> offlineReplicas, boolean isNew) {
        this.leaderEpoch = leaderEpoch;
        this.controllerEpoch = controllerEpoch;
        this.srReplicas = srReplicas;
        this.replicas = replicas;
        this.leaderId = leaderId;
        this.offlineReplicas = offlineReplicas;
        this.isNew = isNew;
    }

    public int getLeaderEpoch() {
        return leaderEpoch;
    }

    public void setLeaderEpoch(int leaderEpoch) {
        this.leaderEpoch = leaderEpoch;
    }

    public int getControllerEpoch() {
        return controllerEpoch;
    }

    public void setControllerEpoch(int controllerEpoch) {
        this.controllerEpoch = controllerEpoch;
    }

    public List<Integer> getSrReplicas() {
        return srReplicas;
    }

    public void setSrReplicas(List<Integer> srReplicas) {
        this.srReplicas = srReplicas;
    }

    public List<Integer> getReplicas() {
        return replicas;
    }

    public void setReplicas(List<Integer> replicas) {
        this.replicas = replicas;
    }

    public int getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }

    public List<Integer> getOfflineReplicas() {
        return offlineReplicas;
    }

    public void setOfflineReplicas(List<Integer> offlineReplicas) {
        this.offlineReplicas = offlineReplicas;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    @Override
    public String toString() {
        return "PartitionState{"
                + "leaderEpoch=" + leaderEpoch
                + ", controllerEpoch=" + controllerEpoch
                + ", srReplicas=" + srReplicas
                + ", replicas=" + replicas
                + ", leaderId=" + leaderId
                + ", offlineReplicas=" + offlineReplicas
                + ", isNew=" + isNew
                + '}';
    }
}
