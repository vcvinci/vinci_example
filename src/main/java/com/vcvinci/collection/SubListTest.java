package com.vcvinci.collection;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author vinci
 * @Title: SubListTest
 * @ProjectName vc-test
 * @Description: TODO
 * @date 2018/8/27下午4:30
 */
public class SubListTest {


    public static void main(String[] args) {
        System.out.println(isNumeric("1.4"));
    }
    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
            if (d > 1) {
                return false;
            }
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}
