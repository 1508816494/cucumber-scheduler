package com.jd.cucumber.scheduler.scanner.job;

import java.util.HashMap;

/**
 * Author xufeng
 * Create Date: 2018/5/23 19:31
 */
public class JobParameter extends HashMap<String,Object> {

    public Integer getInteger(String key) {
        return Integer.valueOf(get(key).toString());
    }

    public Long getLong(String key) {
        return Long.valueOf(get(key).toString());
    }

    public String getString(String key) {
        return get(key).toString();
    }
}
