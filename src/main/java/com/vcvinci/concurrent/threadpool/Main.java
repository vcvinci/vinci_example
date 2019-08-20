package com.vcvinci.concurrent.threadpool;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Callable<String> call = new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("start ...");
                Thread.sleep(3000);
                System.out.println("end ...");
                return "result";
            }
        };

        FutureTask task = new FutureTask(call);

        System.out.println("do other thing");

        String result = (String) task.get();
        System.out.println(result);


    }
}
