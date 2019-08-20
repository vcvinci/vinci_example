package com.vcvinci.delayed;

import com.vcvinci.delayed.Task;
import org.apache.log4j.Logger;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * [�������ϵͳ]
 * <br>
 * [��̨�ػ��̲߳��ϵ�ִ�м�⹤��]
 * </p>
 *
 * @author wangguangdong
 * @version 1.0
 * @Date 2015��11��23��14:19:40
 */
public class TaskQueueDaemonThread {

    private static final Logger LOG = Logger.getLogger(TaskQueueDaemonThread.class);

    private TaskQueueDaemonThread() {
    }

    private static class LazyHolder {
        private static TaskQueueDaemonThread taskQueueDaemonThread = new TaskQueueDaemonThread();
    }

    public static TaskQueueDaemonThread getInstance() {
        return LazyHolder.taskQueueDaemonThread;
    }

    Executor executor = Executors.newFixedThreadPool(20);
    /**
     * �ػ��߳�
     */
    private Thread daemonThread;

    /**
     * ��ʼ���ػ��߳�
     */
    public void init() {
        daemonThread = new Thread(() -> execute());
        daemonThread.setDaemon(true);
        daemonThread.setName("Task Queue Daemon Thread");
        daemonThread.start();
    }

    private void execute() {
        System.out.println("start:" + System.currentTimeMillis());
        while (true) {
            try {
                //���ӳٶ�����ȡֵ,���û�ж�����������һֱ�ȴ���
                Task t1 = t.take();
                if (t1 != null) {
                    //�޸������״̬
                    Runnable task = t1.getTask();
                    if (task == null) {
                        continue;
                    }
                    executor.execute(task);
                    LOG.info("[at task:" + task + "]   [Time:" + System.currentTimeMillis() + "]");
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * ����һ�����Ϊ�յ��� DelayQueue
     */
    private DelayQueue<Task> t = new DelayQueue<>();

    /**
     * �������
     * time �ӳ�ʱ��
     * task ����
     * �û�Ϊ���������ӳ�ʱ��
     */
    public void put(long time, Runnable task) {
        //ת����ns
        long nanoTime = TimeUnit.NANOSECONDS.convert(time, TimeUnit.MILLISECONDS);
        //����һ������
        Task k = new Task(nanoTime, task);
        //����������ӳٵĶ�����
        t.put(k);
    }

    /**
     * ��������
     * @param task
     */
    public boolean endTask(Task<Runnable> task){
        return t.remove(task);
    }
}