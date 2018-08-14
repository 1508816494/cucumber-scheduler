package com.cucumber.scheduler.quartz;


/**
 * Author xufeng
 * Create Date: 2018/6/5 17:00
 */
public class DefaultBeanScheduleManager extends AbstractScheduleManager implements BeanScheduleManager {


    @Override
    public void startupManual(String className, String method, String cron, String name ,String group, String misfirePolicy) {

    }

    @Override
    public void startupManual(String className, String method, String cron, String name, String group) {

    }

    @Override
    public void startupManual(String className, String method, String cron) {

    }
}
