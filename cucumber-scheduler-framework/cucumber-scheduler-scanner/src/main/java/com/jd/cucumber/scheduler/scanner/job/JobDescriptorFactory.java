package com.jd.cucumber.scheduler.scanner.job;

import com.jd.cucumber.scheduler.scanner.annotation.MisfirePolicy;
import com.jd.cucumber.scheduler.scanner.annotation.Schedule;

import java.lang.reflect.Method;

/**
 * Author xufeng
 * Create Date: 2018/5/23 19:29
 */
public class JobDescriptorFactory {
    private JobDescriptorFactory() {}

    public static JobDescriptor jobDescriptor(Class<?> clazz, Method method, boolean hasParameter, Schedule schedule) {
        return new DefaultJobDescriptor(clazz, method, hasParameter, schedule);
    }

    public static JobDescriptor jobDescriptor(Class<?> clazz, Method method, boolean hasParameter, String cron, MisfirePolicy misfirePolicy) {
        return new DefaultJobDescriptor(clazz, method, hasParameter, cron, misfirePolicy);
    }
}
