package com.vcvinci.random;

import java.text.DecimalFormat;
import java.util.Random;

public class RandomTest {

    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat("#.00");

        Random r = new Random();
        double test = r.nextDouble() * r.nextInt(7);
        while (test < 1.0 && test > 6.7) {
            test = r.nextDouble() * r.nextInt(7);
        }
        System.out.println(df.format(test));
    }
}
