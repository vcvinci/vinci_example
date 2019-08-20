package com.vcvinci.common.sr;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月3日 上午6:26:16
 * @description 类说明
 */
public class LeaderSrAndControllerEpoch {

    private int controllerEpoch;
    private LeaderAndSr leaderAndSr;

    public LeaderSrAndControllerEpoch(int controllerEpoch, LeaderAndSr leaderAndSr) {
        this.controllerEpoch = controllerEpoch;
        this.leaderAndSr = leaderAndSr;
    }

    public int getControllerEpoch() {
        return controllerEpoch;
    }

    public void setControllerEpoch(int controllerEpoch) {
        this.controllerEpoch = controllerEpoch;
    }

    public LeaderAndSr getLeaderAndSr() {
        return leaderAndSr;
    }

    public void setLeaderAndSr(LeaderAndSr leaderAndSr) {
        this.leaderAndSr = leaderAndSr;
    }

    @Override
    public String toString() {
        return "LeaderSrAndControllerEpoch{" +
                "controllerEpoch=" + controllerEpoch +
                ", leaderAndSr=" + leaderAndSr +
                '}';
    }
}
