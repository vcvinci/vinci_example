package com.vcvinci.protocol.request;

import com.vcvinci.common.exception.DoveException;
import com.vcvinci.common.exception.ThrowUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 写入消息ack类型
 *
 * @author yankai.zhang
 * @since 2018/7/4 上午10:55
 */
public enum AckType {
    /**
     * 不需要确认
     */
    NoAck((short) 0),

    /**
     * SR全部确认
     */
    SrAck((short) -1),

    /**
     * leader写入磁盘以后确认
     */
    LeaderAck((short) 1);

    private static final Map<Short, AckType> codeCache;

    private static final Throwable initError;

    static {
        Map<Short, AckType> cache = null;
        Throwable throwable = null;
        try {
            cache = new HashMap<>(AckType.values().length);
            for (AckType ackType : AckType.values()) {
                AckType old = cache.put(ackType.code, ackType);
                if (old != null) {
                    throw new DoveException("duplicate ack code found " + ackType.code);
                }
            }
        } catch (Throwable t) {
            // 避免毫无帮助的ClassNotFoundException
            throwable = t;
        }

        codeCache = cache;
        initError = throwable;
    }

    private short code;

    AckType(short code) {
        this.code = code;
    }

    public short getCode() {
        return code;
    }

    public static AckType fromCode(short acks) {
        if (initError != null) {
            throw ThrowUtils.eraseChecked(initError);
        }

        return codeCache.get(acks);
    }
}
