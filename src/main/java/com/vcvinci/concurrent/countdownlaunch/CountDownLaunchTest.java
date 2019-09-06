package com.vcvinci.concurrent.countdownlaunch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author vinci
 * @Title: CountDownLaunch
 * @ProjectName vinci_example
 * @Description: TODO
 * @date 2019/9/24:41 ÏÂÎç
 */
public class CountDownLaunchTest {

    public static void main(String[] args) throws InterruptedException {
        LockCount lockCount = new LockCount();
        LockCount lockCount2 = new LockCount();

        System.out.println(System.currentTimeMillis());
        try {
            TimeUnit.SECONDS.sleep(5);
            System.out.println(System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lockCount.await();
        System.out.println(System.currentTimeMillis());

       /* new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lockCount2.done();
            System.out.println("done");
        }).start();

        lockCount.await();
        System.out.println("test success");*/

    }

    public static class LockCount{
        private CountDownLatch countDownLaunch = new CountDownLatch(1);

        void await () throws InterruptedException {
            countDownLaunch.await();
        }

        void done() {
            countDownLaunch.countDown();
        }
    }
}
