package com.cucumber.scheduler.quartz.bean;

public interface JobBeanFactory {

    <T> T getJobBean(String className);

}
