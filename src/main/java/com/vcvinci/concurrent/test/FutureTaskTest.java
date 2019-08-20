package com.vcvinci.concurrent.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class FutureTaskTest {
    private static final Logger LOG = LoggerFactory.getLogger(FutureTaskTest.class);
    public static void main(String[] args) {
        LOG.info("test{}, etst{}", 1, 3);
        Task task = new Task();// �½��첽����
        FutureTask<Integer> future = new FutureTask<Integer>(task) {
            // �첽����ִ����ɣ��ص�
            @Override
            protected void done() {
                try {
                    System.out.println("future.done():" + get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        // �����̳߳أ�ʹ����Ԥ��������ã�
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(future);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        // ����ȡ���첽����
        // future.cancel(true);

//        try {
//            // �������ȴ��첽����ִ�����-��ȡ�첽����ķ���ֵ
//            System.out.println("future.get():" + future.get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
    }

    // �첽����
    static class Task implements Callable<Integer> {
        // �����첽�����ִ�н��
        @Override
        public Integer call() throws Exception {
            int i = 0;
            for (; i < 10; i++) {
                try {
                    System.out.println(Thread.currentThread().getName() + "_"
                            + i);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return i;
        }
    }


}
