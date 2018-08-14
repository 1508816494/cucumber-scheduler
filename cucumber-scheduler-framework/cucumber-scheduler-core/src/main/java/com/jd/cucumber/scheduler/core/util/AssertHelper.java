package com.jd.cucumber.scheduler.core.util;

public interface AssertHelper {

    /**
     * 检查对象是否为null.
     *
     * @param o 要检查的对象
     * @param message 如果检查失败,则使用该参数作为异常信息
     *
     * @throws IllegalArgumentException 如果检查的对象为{@code null}
     */
    static void notNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 检查对象是否为empty.
     *
     * @param o 要检查的对象
     * @param message 如果检查失败,则使用该参数作为异常信息
     *
     * @throws IllegalArgumentException 如果检查的对象为empty
     */
    static void notEmpty(Object o, String message) {
        if (o == null || o.toString().trim().length() == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 检查结果是否为true
     *
     * @param b 要检查的结果
     * @param message 如果检查失败,则使用该参数作为异常信息
     *
     * @throws IllegalArgumentException 如果检查的结果为false
     */
    static void isTrue(boolean b, String message) {
        if (!b) {
            throw new IllegalArgumentException(message);
        }
    }

}
