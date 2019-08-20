package com.vcvinci.test.concurrent;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vinci
 * @Title: Main
 * @ProjectName vc-test
 * @Description: TODO
 * @date 2018/9/5上午11:04
 */
public class Main {
    public static void main(String[] args) {
        /*Map<Integer, String> map = new ConcurrentHashMap();
        map.put(3, "31111");
        map.put(2, "21111");
        map.put(1, "11111");
        map.put(4, "41111");
        map.put(5, "51111");
        map.put(6, "61111");
        map.put(7, "71111");
        Set<Integer> set = map.keySet();
        for (Integer integer : set) {
            if (integer == 4) {
                map.remove(integer);
            }
        }
        ((ConcurrentHashMap<Integer, String>) map).forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });*/
        System.out.println(new Random().nextInt(3));
        System.out.println(randInt(5,10));
        int c = 5;
        int a = c = 7;
        System.out.println(a + ":" + c);

    }

    public static int randInt(int min, int max) {

        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
