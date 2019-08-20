package com.vcvinci.common.sr;

import java.util.List;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月3日 上午6:26:37
 * @description 类说明
 */
public class LeaderAndSr {

    public static final int NOLEADER = -1;
    public static final int LEADER_TO_DELATE = -2;

    private int leader;
    private int leaderEpoch;
    private List<Integer> srReplicas;

    public LeaderAndSr(int leader, int leaderEpoch, List<Integer> srReplicas) {
        this.leader = leader;
        this.leaderEpoch = leaderEpoch;
        this.srReplicas = srReplicas;
    }

    public int getLeader() {
        return leader;
    }

    public void setLeader(int leader) {
        this.leader = leader;
    }

    public int getLeaderEpoch() {
        return leaderEpoch;
    }

    public void setLeaderEpoch(int leaderEpoch) {
        this.leaderEpoch = leaderEpoch;
    }

    public List<Integer> getSrReplicas() {
        return srReplicas;
    }

    public void setSrReplicas(List<Integer> srReplicas) {
        this.srReplicas = srReplicas;
    }

    @Override
    public String toString() {
        return "LeaderAndSr{" +
                "leader=" + leader +
                ", leaderEpoch=" + leaderEpoch +
                ", srReplicas=" + srReplicas +
                '}';
    }
}
