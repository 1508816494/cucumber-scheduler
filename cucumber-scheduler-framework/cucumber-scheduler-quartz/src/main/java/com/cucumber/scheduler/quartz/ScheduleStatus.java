package com.cucumber.scheduler.quartz;

/**
 * 调度状态.
 *
 * @author Xiaolong Zuo
 * @since 0.9.3
 */
public enum ScheduleStatus {

    /**
     * 代表该任务已经启动
     */
    STARTUP,
    /**
     * 代表该任务曾经被启动,但当前处于暂停状态
     */
    PAUSE,
    /**
     * 代表该任务从未被启动
     */
    SHUTDOWN

}
