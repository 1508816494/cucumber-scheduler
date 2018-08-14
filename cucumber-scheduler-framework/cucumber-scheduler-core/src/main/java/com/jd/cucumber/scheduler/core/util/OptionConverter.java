package com.jd.cucumber.scheduler.core.util;

/**
 * Author xufeng
 * Create Date: 2018/6/8 16:37
 */
public class OptionConverter {

    public static String getSystemProperty(String key, String def) {
        try {
            return System.getProperty(key, def);
        } catch (Throwable var3) {
            return def;
        }
    }

    public static boolean toBoolean(String value, boolean dEfault) {
        if (value == null) {
            return dEfault;
        } else {
            String trimmedVal = value.trim();
            if ("true".equalsIgnoreCase(trimmedVal)) {
                return true;
            } else {
                return "false".equalsIgnoreCase(trimmedVal) ? false : dEfault;
            }
        }
    }
}
