package com.jd.cucumber.scheduler.scanner.job;

import com.jd.cucumber.scheduler.scanner.annotation.MisfirePolicy;
import com.jd.cucumber.scheduler.scanner.annotation.Schedule;

import java.lang.reflect.Method;

/**
 * 默认的任务描述符实现
 *
 */
public class DefaultJobDescriptor implements JobDescriptor {

    private String group;

    private String name;

    private Method method;

    private Class<?> clazz;

    private boolean hasParameter;

    protected String cron;

    protected MisfirePolicy misfirePolicy;

    public DefaultJobDescriptor(Class<?> clazz, Method method, boolean hasParameter, Schedule schedule) {
        this(clazz, method, hasParameter, schedule.cron(), schedule.misfirePolicy());
    }

    public DefaultJobDescriptor(Class<?> clazz, Method method, boolean hasParameter, String cron, MisfirePolicy misfirePolicy) {
        this.group = clazz.getName();
        this.name = method.getName();
        this.clazz = clazz;
        this.method = method;
        this.hasParameter = hasParameter;
        this.cron = cron;
        this.misfirePolicy = misfirePolicy;
    }

    @Override
    public String group() {
        return group;
    }

    @Override
    public String name() {
        return name;
    }


    @Override
    public Method method() {
        return method;
    }

    @Override
    public boolean hasParameter() {
        return hasParameter;
    }

    @Override
    public Class<?> clazz() {
        return clazz;
    }

    @Override
    public String cron() {
        return cron;
    }

    @Override
    public MisfirePolicy misfirePolicy() {
        return misfirePolicy;
    }

    @Override
    public String toString() {
        return "JobDescriptor:{" +
                "method=" + method +
                ", hasParameter=" + hasParameter +
                ", cron='" + cron + '\'' +
                ", misfirePolicy=" + misfirePolicy +
                '}';
    }

}
