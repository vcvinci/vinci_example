package com.vcvinci.java8;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author vinci
 * @Title: FlatMapTest
 * @ProjectName Leecode
 * @Description: TODO
 * @date 2019-03-2110:56
 */
public class FlatMapTest {
    public static void main(String[] args) {
        //  test1
        List<String> words = Arrays.asList("hello", "test");
        List<String[]> collect = words.stream().map(word -> word.split("")).collect(Collectors.toList());
        //collect.stream().forEach(arg -> Arrays.asList(arg).stream().forEach(System.out::println));
        List<String> collect1 =
                words.stream()
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
        collect1.stream().forEach(System.out::println);
        System.out.println("===============finish test 1=================");

        // test2
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(5, 6);

        List<int[]> collect2 = list1.stream().flatMap(a -> list2.stream().map(b -> new int[]{a, b})).collect(Collectors.toList());
        collect2.stream().forEach(arr -> System.out.println(arr[0] + ":" + arr[1]));
    }
}
