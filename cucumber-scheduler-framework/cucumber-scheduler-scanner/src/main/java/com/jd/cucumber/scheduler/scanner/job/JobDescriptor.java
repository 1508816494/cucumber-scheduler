package com.jd.cucumber.scheduler.scanner.job;

import com.jd.cucumber.scheduler.scanner.annotation.MisfirePolicy;
import java.lang.reflect.Method;

/**
 * Author xufeng
 * Create Date: 2018/5/23 19:19
 */
public interface JobDescriptor {
    String group();

    String name();

    Method method();

    boolean hasParameter();

    Class<?> clazz();

    String cron();

    MisfirePolicy misfirePolicy();
}
