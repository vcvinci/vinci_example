package com.vcvinci.common.exception;

/**
 * @author yankai.zhang
 * @since 2018/7/4 上午11:41
 */
public final class ThrowUtils {

    private ThrowUtils() {
    }

    /**
     * 将受检异常转换为运行时异常，调用方式：<br/>
     * <code>
     * throw ThrowUtils.eraseChecked(e);
     * </code>
     *
     * @param t 任何异常或错误
     * @return 不返回
     */
    public static RuntimeException eraseChecked(Throwable t) {
        if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        }
        if (t instanceof Error) {
            throw (Error) t;
        }
        if (t instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }
        throw new RuntimeException(t);
    }
}
