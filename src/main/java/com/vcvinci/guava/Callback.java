package com.vcvinci.guava;

import com.google.common.util.concurrent.*;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: vinci.d
 * @Date: 2018/6/9 15:52
 * @Description:
 */
public class Callback {
    private static ExecutorService es = Executors.newFixedThreadPool(3);
    private static ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(es);

    public static void main(String[] args) {
        /*ListenableFuture listenableFuture = listeningExecutorService.submit(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                if (new Random().nextInt(3) == 2) {
                    throw new NullPointerException();
                }
                return 1;
            }
        });
*/
        ListenableFuture listenableFuture = listeningExecutorService.submit(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                if (new Random().nextInt(3) == 2) {
                    throw new NullPointerException();
                }
                return 1;
            }
        });

        FutureCallback futureCallback = new FutureCallback<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                System.out.println("---------" + result);
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("==============" + t.getMessage());
            }
        };

        Futures.addCallback(listenableFuture, futureCallback);
    }



}
