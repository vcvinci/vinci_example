package com.vcvinci.common.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @CreateDate: 2018/11/5 20:53
 * @Author: zhengquan.lin@ucarinc.com
 * @Description:
 */
public abstract class ShutdownableThread extends Thread {

    public static final Logger LOGGER = LoggerFactory.getLogger(ShutdownableThread.class);
    private boolean isInterruptible;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final CountDownLatch shutdownLatch = new CountDownLatch(1);

    public ShutdownableThread(final String threadName) {
        this(threadName, true);
    }

    public ShutdownableThread(final String threadName, final boolean isInterruptible) {
        this.isInterruptible = isInterruptible;
        this.setName(threadName);
        this.setDaemon(false);
        //this.setUncaughtExceptionHandler(uncaughtExceptionHandler);
    }

    public void shutdown() throws InterruptedException {
        LOGGER.info("正在关闭线程");
        if (running.compareAndSet(true, false)) {
            if (isInterruptible) {
                LOGGER.info("执行强制中断线程");
                this.interrupt();
            }
        }
        awaitShutdown();
    }

    public void awaitShutdown() throws InterruptedException {
        shutdownLatch.await();
        LOGGER.info("成功关闭线程");
    }

    public void pause(int timeout, final TimeUnit timeUnit){
        try {
            if (shutdownLatch.await(timeout, timeUnit)) {
                LOGGER.info("暂停线程时出现线程已经关闭了");
            }
        } catch (InterruptedException e) {
            LOGGER.error("暂停线程出现中断异常：", e);
        }
    }

    /**
     * 留给子类实现各种的业务逻辑处理方法
     */
    public abstract void execute();

    /**
     * 如果线程尚未退出且没有任何关闭方法，则返回true
     *
     * @return
     */
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void run() {
        LOGGER.info("启动线程");
        try {
            while (isRunning()) {
                execute();
            }
        } catch (Error | RuntimeException e) {
            LOGGER.error("线程 {} 执行出现异常: ", e);
            throw e;
        } finally {
            // 线程结束
            shutdownLatch.countDown();
        }
        LOGGER.info("关闭线程");
    }

    /**
     * 优雅的关闭线程，首先尝试使用指定的超时时间进行正常关闭，
     * 如果失败， 则使用强制中断线程。
     *
     * @param gracefulTimeout 超时时间, 不能 小于 0
     * @param timeUnit        单位
     */
    public void gracefulShutDownWithTimeout(final long gracefulTimeout, final TimeUnit timeUnit) throws InterruptedException {
        if (gracefulTimeout <= 0) {
            throw new IllegalArgumentException("gracefulTimeout 必须大于0");
        }
        LOGGER.info("开始优雅的关闭线程 {} ");
        running.set(false);
        boolean success = shutdownLatch.await(gracefulTimeout, timeUnit);
        // 超时等待时间内，关闭失败，就调用强制关闭
        if (!success) {
            LOGGER.info("线程 {} 被强制关闭");
            // 中断线程
            interrupt();
        }
    }
}
