package com.vcvinci.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @CreateDate: 2018/11/5 21:14
 * @Author: zhengquan.lin@ucarinc.com
 * @Description:
 */
public class ThreadUtils {

    /**
     * 创建一个新的线程工厂
     *
     * @param pattern 要使用的模式。如果这包含％d，它将被替换为一个线程号。它不应包含多于*％的d
     * @param daemon
     * @return
     */
    public static ThreadFactory createThreadFactory(final String pattern, final boolean daemon) {
        return new ThreadFactory() {
            private final AtomicLong threadEpoch = new AtomicLong(0);

            @Override
            public Thread newThread(Runnable r) {
                String threadName;
                if (pattern.contains("%d")) {
                    threadName = String.format(pattern, threadEpoch.addAndGet(1));
                } else {
                    threadName = pattern;
                }
                Thread thread = new Thread(r, threadName);
                thread.setDaemon(daemon);
                return thread;
            }
        };
    }
}
