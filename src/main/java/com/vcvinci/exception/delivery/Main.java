package com.vcvinci.exception.delivery;

/**
 * @Auther: Administrator
 * @Date: 2018/7/5 14:04
 * @Description:
 */
public class Main {
    public static void main(String[] args) {
        ClientException clientException = new ClientException("testset");
        throwThrowable(clientException);
    }

    public static void throwRunTime(RuntimeException e) {
        if (e instanceof ClientException) {
            System.out.println("runtime client exception");
        } else {
            throw e;
        }
    }

    public static void throwThrowable(Throwable e) {
        throwRunTime((RuntimeException) e);
    }

}
