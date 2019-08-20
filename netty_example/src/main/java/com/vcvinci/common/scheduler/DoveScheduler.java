package com.vcvinci.common.scheduler;

import com.vcvinci.common.thread.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月3日 上午8:15:50
 * @description 类说明
 */
public class DoveScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoveScheduler.class);

    /**
     * 核心线程池的线程数
     */
    private int threadsNum;
    private boolean daemon;
    private final String threadNamePrefix;
    private ScheduledThreadPoolExecutor executor;

    public DoveScheduler(final int threadsNum, final String threadNamePrefix) {
        this(threadsNum, threadNamePrefix, true, true);
    }

    public DoveScheduler(final int threadsNum, final String threadNamePrefix, final boolean daemon, final boolean startup) {
        this.threadsNum = threadsNum;
        this.threadNamePrefix = threadNamePrefix;
        this.daemon = daemon;
        if (startup) {
            this.startup();
        }
    }

    public void startup() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("开始初始化任务调度程序: DoveScheduler, threadNamePrefix={}", threadNamePrefix);
        }
        synchronized (this) {
            // 确保只能初始化一次
            if (this.isActive()) {
                throw new IllegalStateException("此调度程序已经启动, 不能重复实例化！threadNamePrefix={}" + threadNamePrefix);
            }
            executor = new ScheduledThreadPoolExecutor(threadsNum);
            // 在调用线程池 shutdown() 方法后，是否继续执行现有 周期任务（通过 scheduleAtFixedRate、scheduleWithFixedDelay 提交的周期任务）的策略；默认值为: false；
            executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
            // 在调用线程池 shutdown() 方法后，是否继续执行现有 延时任务，就是通过 schedule()方法提交的延时任务 ）的策略, 默认值为: true
            executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
            executor.setThreadFactory(new NamedThreadFactory(this.threadNamePrefix, this.daemon));
        }
    }

    public void shutdown() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("正在关闭任务调度程序: DoveScheduler, threadNamePrefix={}", threadNamePrefix);
        }
        ExecutorService cachedExecutor = this.executor;
        if (cachedExecutor != null) {
            synchronized (this) {
                cachedExecutor.shutdown();
                this.executor = null;
            }
            try {
                cachedExecutor.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                LOGGER.error("关闭 DoveScheduler 失败, threadNamePrefix={}", threadNamePrefix, e);
            }
        }
    }

    /**
     * 触发执行一次
     *
     * @param scheduleTask
     */
    public ScheduledFuture<?> scheduleOnce(final String taskName, final Runnable scheduleTask) {
        return this.schedule(taskName, scheduleTask, 0L, -1L, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> scheduledOnceWithDealyMs(final String taskName, final Runnable scheduleTask, final long delay) {
        return this.schedule(taskName, scheduleTask, delay, -1L, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> scheduledOnceWithDealy(final String taskName, final Runnable scheduleTask, final long delay, final TimeUnit timeUnit) {
        return this.schedule(taskName, scheduleTask, delay, -1L, timeUnit);
    }

    /**
     * 任务定时调度
     *
     * @param scheduleTask
     * @param delay         调度任务，加入调度队列后，延迟 delay 时长才会被调度执行
     * @param period        每隔多久执行一次；小于0时，只会触发一次
     * @param timeUnit
     */
    public ScheduledFuture<?> schedule(final String taskName, final Runnable scheduleTask, final long delay, final long period, final TimeUnit timeUnit) {
        this.ensureRunning();
        // 避免线程抛异常时，任务调度失效, 做了一层封装
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    //LOGGER.debug("开始调度任务[{}]执行", taskName);
                    scheduleTask.run();
                } catch (Exception e) {
                    //LOGGER.error("Uncaught exception in scheduled task[{}]", taskName, e);
                } finally {
                    //LOGGER.debug("完成调度任务[{}]执行", taskName);
                }
            }
        };
        if (period >= 0) {
            return this.executor.scheduleAtFixedRate(runnable, delay, period, timeUnit);
        } else {
            return this.executor.schedule(runnable, delay, timeUnit);
        }
    }

    public Boolean isActive() {
        synchronized (this) {
            return this.executor != null;
        }
    }

    private void ensureRunning() {
        if (!this.isActive()) {
            throw new IllegalStateException("DoveScheduler 未启动运行");
        }
    }

    // for dynamic config
    public void resizeCoreThreadPool(int coreSize) {
        executor.setCorePoolSize(coreSize);
    }
}
