package com.vcvinci.common.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @CreateDate: 2018/11/5 21:11
 * @Author: zhengquan.lin@ucarinc.com
 * @Description:
 */
public class DoveThread extends Thread {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static DoveThread daemon(final String name, Runnable runnable) {
        return new DoveThread(name, runnable, true);
    }

    public static DoveThread nonDaemon(final String name, Runnable runnable) {
        return new DoveThread(name, runnable, false);
    }

    public DoveThread(final String name, boolean daemon) {
        super(name);
        configureThread(name, daemon);
    }

    public DoveThread(final String name, Runnable runnable, boolean daemon) {
        super(runnable, name);
        configureThread(name, daemon);
    }

    private void configureThread(final String name, boolean daemon) {
        setDaemon(daemon);
        setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                logger.error("Uncaught exception in thread '{}':", name, e);
            }
        });
    }

}
