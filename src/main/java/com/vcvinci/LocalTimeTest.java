package com.vcvinci;

/**
 * @author vinci
 * @Title: LocalTimeTest
 * @ProjectName Leecode
 * @Description: TODO
 * @date 2019-04-1910:25
 */
public class LocalTimeTest {
    public static void main(String[] args) {
        long time = 1555643012000L;
        long result = time/1000/60 * 1000*60;
        System.out.println(result);
    }
}
