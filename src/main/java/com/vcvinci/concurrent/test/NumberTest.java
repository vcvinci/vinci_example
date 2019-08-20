package com.vcvinci.concurrent.test;

import java.util.Random;

public class NumberTest {

    public static void main(String[] args) {
        /*int number = 10;
        printInfo(number);
        number = number << 1;
        printInfo(number);
        number = number >> 1;
        printInfo(number);*/
        Random random = new Random(10);
        for (int i = 0; i < 3; i++) {
            System.out.println(random.nextInt());
        }
    }

    private static void printInfo(int num) {
        System.out.println(Integer.toBinaryString(num));
    }

}
