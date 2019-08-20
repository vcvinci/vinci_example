package com.vcvinci.exception;

/**
 * @Auther: Administrator
 * @Date: 2018/7/4 15:00
 * @Description:
 */
public class RuntimeExceptionTest {

    public static void main(String[] args) {
        cacheRuntimeExcepiton();
    }

    public static void cacheRuntimeExcepiton() {
        try {
            throwRuntimeException();
        } catch (RuntimeException e) {
            setException(e);
        }
    }

    public static void throwRuntimeException() {
        throw new RuntimeException("test runtime excepiton");
    }

    public static void setException(RuntimeException e) {
        System.out.println(e);
    }
}
