package com.vcvinci.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: Administrator
 * @Date: 2018/6/28 14:24
 * @Description:
 */
public class RegexTest {
    public static final String regexIpPort = "(^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\." +
            "(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}" +
            "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])" +
            ":\\d{0,5}$)";

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile(regexIpPort);
        Matcher matcher = pattern.matcher("24.256.123.12:890");
        if (!matcher.matches()) {
            System.out.println("false");
        } else {
            System.out.println("true");
        }
    }
}
