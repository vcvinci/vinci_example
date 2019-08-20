package com.vcvinci.common.util;

import com.ucarinc.dove.util.Util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月23日 下午5:32:42
 * @description 类说明
 */
public class IdGenerator {

    public static AtomicInteger integer = new AtomicInteger(0);

    public static int generate() {
        return Util.toPositive(integer.incrementAndGet());
    }
}
 