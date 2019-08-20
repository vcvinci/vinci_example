package com.vcvinci.concurrent.threadpool;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolTest {

    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    private static final int STOP       =  1 << COUNT_BITS;
    private static final int TIDYING    =  2 << COUNT_BITS;
    private static final int TERMINATED =  3 << COUNT_BITS;

    public static void main(String[] args) {
        ThreadPoolTest test = new ThreadPoolTest();
        System.out.println(test.ctl.get());// -536870912
        System.out.println(Integer.toBinaryString(test.ctl.get())); // 11100000000000000000000000000000
        System.out.println("------------");
        System.out.println(Integer.toBinaryString(SHUTDOWN) + "[SHUTDOWN]" );
        System.out.println( Integer.toBinaryString(RUNNING) + "[RUNNING]");
        System.out.println(Integer.toBinaryString(STOP) + "[STOP]");
        System.out.println(Integer.toBinaryString(TIDYING) + "[TIDYING]");
        System.out.println(Integer.toBinaryString(TERMINATED) + "[TERMINATED]");
        System.out.println("------------");
        System.out.println("CAPACITY:"+ CAPACITY);
        System.out.println(Integer.toBinaryString(CAPACITY) + "[CAPACITY]");
        System.out.println("------------");
        System.out.println(15 ^ 0);
        System.out.println(Integer.toBinaryString(15));

        System.out.println(workerCountOf(test.ctl.get()));
        System.out.println("------------");
        retry();

    }
    private static int runStateOf(int c)     { return c & ~CAPACITY; }
    private static int workerCountOf(int c)  { return c & CAPACITY; }
    private static int ctlOf(int rs, int wc) { return rs | wc; }
    public static void retry() {
        /*int count = 0;
        retry:
        for (; ; ) {
            count = 0;
            for (; ; ) {
                count++;
                System.out.println("count==" + count);
                if (count % 5 == 0) {
                    continue retry;
                }
            }
        }*/
        retry00:// 1<span style="font-family:Arial, Helvetica, sans-serif;">（行2）</span>
        for (int i = 0; i < 10; i++) {
            //retry:// 2（行4）
            while (i == 5) {
                continue retry00;
            }
            System.out.print(i + " ");
        }
    }
}
