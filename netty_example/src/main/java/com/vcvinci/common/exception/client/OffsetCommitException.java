package com.vcvinci.common.exception.client;

/**
 * @Auther: yuxin.chen01
 * @Date: 2018/12/6 18:12
 * @Description: 当提交offset时,如果得到unknownMember, generationIllegal, 或者GroupInRebalance响应则抛出该异常;
 * 碰到以上异常则说明已经发生rebalance,分区已经被分配给其他的消费者, 所以无需继续重试提交offset
 */
public class OffsetCommitException extends ClientException {

	public OffsetCommitException() {
        this("OffsetCommit cannot be completed since the group has already " +
                "rebalanced and assigned the partitions to other consumers. This means that the time " +
                "between subsequent calls to poll() was longer than the configured max.poll.interval.ms, " +
                "which implies that the poll loop is spending too much time processing message. " +
                "You can fix this problem by setting the session timeout longer or by reducing the maximum " +
                "size of batches returned in poll() with max.poll.records.");
	}

	public OffsetCommitException(String msg){
	    super(msg);
    }

}
