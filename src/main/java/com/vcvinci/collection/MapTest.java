package com.vcvinci.collection;

import java.text.ParseException;
import java.util.List;

public class MapTest {
    public static void main(String[] args) throws ParseException {
        double x = (5d / (5d+6d));
        System.out.println(x);


        double result1 = (10 * 4 / (5+4)) / 3 + 5;
        double result2 = (10 * 5 / (5+4)) / 3 + 5 ;
        System.out.println(result1 + ":" + result2);

       // int r = (((int) Math.floor(flow_ratio * (double) partitions.size()) - partitionsInIdc) / idc_ratio) * idc_ratio + partitionsInIdc;

    }

    public static void printStr(List list1){
        for (int i = 0; i < list1.size(); i++) {
            System.out.println(list1.get(i));
        }
    }
}
