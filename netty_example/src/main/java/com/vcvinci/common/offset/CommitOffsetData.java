package com.vcvinci.common.offset;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月4日 上午8:16:13
 * @description 类说明
 */
public class CommitOffsetData {

    public static final long INVALID_OFFSET = -1L;
    public static final String NO_METADATA = "";
    public static final CommitOffsetData INVALID_OFFSET_METADATA = new CommitOffsetData(INVALID_OFFSET, NO_METADATA);

    // XXX 客户端提交的值是 绝对值还是相对值？
    private long commitOffset;
    //目前暂定传memberId
    private String metadata;

    public CommitOffsetData(long commitOffset, String metadata) {
        this.commitOffset = commitOffset;
        this.metadata = StringUtils.isEmpty(metadata) ? NO_METADATA : metadata;
    }

    public CommitOffsetData(long commitOffset) {
        this.commitOffset = commitOffset;
        this.metadata = NO_METADATA;
    }

    /**
     * 转换为 OffsetMetadata
     * @param currentTimeMillis 同一个提交批次，用统一当前系统时间
     * @return
     */
    public OffsetMetadata buildOffsetMetadata(long currentTimeMillis) {
        return new OffsetMetadata(this, currentTimeMillis);

    }

    public long getCommitOffset() {
        return commitOffset;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setCommitOffset(long commitOffset) {
        this.commitOffset = commitOffset;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommitOffsetData)) return false;
        CommitOffsetData that = (CommitOffsetData) o;
        return getCommitOffset() == that.getCommitOffset() &&
                Objects.equals(getMetadata(), that.getMetadata());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommitOffset(), getMetadata());
    }

    @Override
    public String toString() {
        return "CommitOffsetData{"
                + "commitOffset=" + commitOffset
                + ", metadata='" + metadata + '\''
                + '}';
    }
}
 