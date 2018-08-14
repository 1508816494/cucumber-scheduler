package com.cucumber.scheduler.quartz;

/**
 * Author xufeng
 * 针对spring环境下，配置文件的bean管理
 * Create Date: 2018/6/5 17:00
 */
public interface BeanScheduleManager extends ScheduleManager{

    void startupManual(String className, String method, String cron, String name ,String group, String misfirePolicy);
    void startupManual(String className, String method, String cron, String name ,String group);
    void startupManual(String className, String method, String cron);

}
