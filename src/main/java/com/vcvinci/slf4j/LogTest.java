package com.vcvinci.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @Auther: Administrator
 * @Date: 2018/6/11 15:49
 * @Description:
 */
public class LogTest {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(LogTest.class);
        logger.info("hello {}", new Date());
        System.out.println(LogTest.class.getName());
    }
}
