package com.vcvinci.common.offset;

import java.util.Objects;

/**
 * @CreateDate: 2018/11/20 11:33
 * @Author: zhengquan.lin@ucarinc.com
 * @Description:
 */
public class OffsetMetadata {

    private CommitOffsetData partitionData;

    private long commitTimestamp;

    public OffsetMetadata(CommitOffsetData partitionData, long commitTimestamp) {
        this.partitionData = partitionData;
        this.commitTimestamp = commitTimestamp;
    }

    public CommitOffsetData currentPartitionData() {
        return partitionData;
    }

    public long getCommitTimestamp() {
        return commitTimestamp;
    }


    public CommitOffsetData getPartitionData() {
        return partitionData;
    }

    public void setPartitionData(CommitOffsetData partitionData) {
        this.partitionData = partitionData;
    }

    public void setCommitTimestamp(long commitTimestamp) {
        this.commitTimestamp = commitTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OffsetMetadata)) return false;
        OffsetMetadata that = (OffsetMetadata) o;
        return getCommitTimestamp() == that.getCommitTimestamp() &&
                Objects.equals(getPartitionData(), that.getPartitionData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPartitionData(), getCommitTimestamp());
    }

    @Override
    public String toString() {
        return String.format("[%s,commitTimestamp: %d]", partitionData, commitTimestamp);
    }
}
