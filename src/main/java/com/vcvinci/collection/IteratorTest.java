package com.vcvinci.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2018/7/5 14:41
 * @Description:
 */
public class IteratorTest {
    public static void main(String[] args) {
        List<Integer> old = getIntegers(1, "d");
        List<Integer> current = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        current.removeAll(old);
        System.out.println("==========================");
        current.forEach(System.out::println);
    }

    /**
    　* @Description: TODO
    　* @param [a, b]
    　* @return java.util.List<java.lang.Integer>
    　* @throws
    　* @author vinci
    　* @date 2018/8/16 下午2:48
    　*/
    private static List<Integer> getIntegers(int a, String b) {
        System.out.println(a + "b");
        List<Integer> old = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
        for (Iterator<Integer> iterator = old.iterator(); iterator.hasNext();) {
            Integer integer = iterator.next();
            if (integer < 3) {
                iterator.remove();
            }
        }
        return old;
    }
}
