package com.vcvinci.remote.stats;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author yupeng.sun@ucarinc.com
 * @version 创建时间：2018年1月26日 上午8:11:04
 * @Description
 */
public class RuntimeStats {

    private AtomicInteger connAsClientCount = new AtomicInteger(0);
    private AtomicInteger connAsServerCount = new AtomicInteger(0);
    private AtomicLong sendRequestCount = new AtomicLong(0);
    private static final RuntimeStats instance = new RuntimeStats();

    private RuntimeStats() {

    }

    public static RuntimeStats getInstance() {
        return instance;
    }

    public void decreaseConnAsClientCount() {
        connAsClientCount.incrementAndGet();
    }

    public void increaseConnAsClientCount() {
        connAsClientCount.decrementAndGet();
    }

    public void decreaseConnAsServerCount() {
        connAsServerCount.incrementAndGet();
    }

    public void increaseConnAsServerCount() {
        connAsServerCount.decrementAndGet();
    }

    public void increaseSendRequestCount() {
        sendRequestCount.incrementAndGet();
    }
}
