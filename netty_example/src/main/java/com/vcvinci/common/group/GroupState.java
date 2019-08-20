package com.vcvinci.common.group;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 下午7:46:53
 * @description 类说明
 */
public enum GroupState {

    /**
     * 状态:  准备rebalance, 如果有心跳或者同步group请求,则返回REBALANCE_IN_PROGRESS错误码
     * 状态转换:
     * =>AwaitingSync: 超时之前所有的consumer都加入组, 或者延迟加入组请求超时(移除过期未发送请求的成员), 状态转为同步组状态
     * =>Empty: 接受到LeaveGroup请求, 所有的成员离开组
     * =>Dead: 分区迁移,group移除
     */
    PreparingRebalance,

    /**
     * 状态:  等待同步, 接收到所有消费组中消费者的join请求,或者delayedJoin超时.等待consumer leader发送同步组请求(携带分区分配结果).
     * 状态转换:
     * =>PreparingRebalance: 接收到 joinGroup/leaveGroup/consumer心跳超时
     * =>Running: 接收到consumerLeader的分区分配信息
     * =>Dead: 分区迁移,group移除
     */
    AwaitingSync,

    /**
     * 状态: 稳定状态,正常运行状态,对于同步组请求正常返回分区分配信息, 可以正常响应offset拉取和提交请求(group generation一致)
     * 状态转换:
     * =>PreparingRebalance: consumer failure/consumer leave/ leader join group/ follower带有新的请求元数据(?待补充)
     * =>Dead: 分区迁移移除group=>dead
     */
    Running,

    /**
     * 状态: 没有consumer 并且元数据被移除
     * 响应 /join group/sync group/leave group/offset commit/心跳=> UNKNOWN_MEMBER_ID 允许fetch offset 请求
     */
    Dead,

    /**
     * 状态:  没有consumer, 等带offsets过期
     * 响应: 正常响应join group请求, 响应sync group/心跳/leave group/offset commit UNKNOWN_MEMBER_ID, 允许拉取 offset 请求
     * 状态转换:
     * =>Dead: 最后一个offset元数据被移除/分区移除导致group移除/group元数据过期移除
     * =>PreparingRebalance: 新的consumer加入组
     */
    Empty
}
