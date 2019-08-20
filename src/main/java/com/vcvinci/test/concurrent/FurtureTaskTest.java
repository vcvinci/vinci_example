package com.vcvinci.test.concurrent;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @author vinci
 * @Title: FurtureTaskTest
 * @ProjectName vc-test
 * @Description: TODO
 * @date 2018/9/6上午10:02
 */
public class FurtureTaskTest {
    public static void main(String[] args) throws InterruptedException {

        test();
    }

    private static void test() throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            FutureTask<Boolean> task = new FutureTask<Boolean>(() -> {
                TimeUnit.SECONDS.sleep(30);
                int num = (int)(Math.random()*100);
                System.out.println(Thread.currentThread().getName() + ":" + num);
                if (num % 2 == 0) {
                    return true;
                } else {
                    return false;
                }
            }){
                @Override
                protected void done() {
                    try {
                        if (!get()) {
                            System.out.println("direct return failed!!!");
                            return;
                        }
                    } catch (InterruptedException e) {
                    } catch (ExecutionException e) {
                    }
                }
            };
            executor.execute(task);

        }
        executor.shutdown();
    }
}
