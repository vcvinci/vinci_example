package com.vcvinci.exception;

import java.util.stream.IntStream;

/**
 * @Auther: Administrator
 * @Date: 2018/6/12 16:35
 * @Description:
 */
public class ExceptionTest {

    public static void main(String[] args) {

        try {
            IntStream.range(1, 8).forEach(n -> {
                if (n == 5) {
                    throw new RuntimeException("num is wrong!!");
                }
                System.out.println(n);
            });
        } catch (Exception e) {
            System.out.println("num is wrong!!");
        }

    }
}
