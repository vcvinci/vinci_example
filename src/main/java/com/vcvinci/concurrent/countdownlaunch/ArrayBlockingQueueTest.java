package com.vcvinci.concurrent.countdownlaunch;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

/**
 * @author vinci
 * @Title: ArrayBlockingQueueTest
 * @ProjectName vinci_example
 * @Description: TODO
 * @date 2019/9/47:21 обнГ
 */
public class ArrayBlockingQueueTest {
    public static void main(String[] args) {
        BlockingQueue<Object> queue = new ArrayBlockingQueue<>(8);
        IntStream.range(0, 10).forEach(
                queue::offer
        );
        queue.forEach(System.out::println);
    }
}
