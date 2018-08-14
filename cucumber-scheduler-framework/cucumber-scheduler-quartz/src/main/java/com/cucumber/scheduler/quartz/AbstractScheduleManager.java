package com.cucumber.scheduler.quartz;

import com.jd.cucumber.scheduler.core.exception.CucumberException;
import com.jd.cucumber.scheduler.core.util.ClassHelper;
import com.jd.cucumber.scheduler.core.util.LoggerHelper;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的抽象调度管理器,实现了最基本的调度管理器共有的功能.
 *
 * @author Xiaolong Zuo
 * @since 0.9.4
 */
public abstract class AbstractScheduleManager implements ScheduleManager {

    protected Map<String, ScheduleStatus> jobStatusMap;

    protected Map<String, List<String>> groupNameListMap;

    protected List<String> groupList;

    protected Properties properties;

    protected Scheduler scheduler;

    @Override
    public Scheduler get() {
        return this.scheduler;
    }

    public AbstractScheduleManager() {
        this.groupNameListMap = new ConcurrentHashMap<>();
        this.groupList = new ArrayList<>();
        this.jobStatusMap = new ConcurrentHashMap<>();
    }

    protected void initScheduler(Properties properties) {
        this.properties = properties;
        try {
            StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
            schedulerFactory.initialize(properties);
            scheduler = schedulerFactory.getScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            LoggerHelper.error("create scheduler failed.", e);
            throw new CucumberException(e);
        }
    }

    protected String getUniqueId(JobKey jobKey) {
        return ClassHelper.getUniqueDescriptor(jobKey.getGroup(), jobKey.getName());
    }

    @Override
    public ScheduleStatus getScheduleStatus(String group, String name) {
        if (jobStatusMap == null) {
            return ScheduleStatus.SHUTDOWN;
        }
        return jobStatusMap.get(getUniqueId(JobKey.jobKey(name, group)));
    }

    @Override
    public List<String> getGroupList() {
        if (groupList == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(groupList);
    }

    @Override
    public List<String> getNameList(String group) {
        if (groupNameListMap == null) {
            return Collections.emptyList();
        }
        List<String> nameList = groupNameListMap.get(group);
        if (nameList == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(nameList);
    }

    @Override
    public synchronized void shutdown() {
        getGroupList().forEach(this::shutdown);
    }

    @Override
    public synchronized void shutdown(String group) {
        for (String name : getNameList(group)) {
            shutdown(group, name);
        }
    }

    @Override
    public synchronized void shutdown(String group, String name) {
        JobKey jobKey = JobKey.jobKey(name, group);
        ScheduleStatus scheduleStatus = jobStatusMap.get(getUniqueId(jobKey));
        if (scheduleStatus != ScheduleStatus.STARTUP) {
            LoggerHelper.warn("group [" + group + "," + name + "] has been paused.");
            return;
        }
        try {
            if (scheduler != null && scheduler.isStarted()) {
                scheduler.pauseJob(jobKey);
                LoggerHelper.info("group [" + group + "," + name + "] has been paused successfully.");
            } else {
                LoggerHelper.info("scheduler has been shutdown ,ignore the pause operation for [" + group + "," + name + "]");
            }
        } catch (SchedulerException e) {
            LoggerHelper.error("pause [" + group + "," + name + "] job failed.", e);
            throw new CucumberException(e);
        }
        jobStatusMap.put(getUniqueId(jobKey), ScheduleStatus.PAUSE);
    }

}
