package com.vcvinci.concurrent.test;

public class FinallySwallowException {

    public static void main(String[] args) throws Exception {
        System.out.println(swallowException()); // 打印出2，而不是打印出异常栈
    }

    public static int swallowException() throws Exception {
        try {
            throw new Exception();
        } finally {
            return 2;
        }
    }
}
