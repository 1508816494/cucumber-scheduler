
package com.cucumber.scheduler.quartz;

import com.cucumber.scheduler.quartz.bean.JobBeanFactory;
import com.jd.cucumber.scheduler.core.exception.CucumberException;
import com.jd.cucumber.scheduler.core.util.ClassHelper;
import com.jd.cucumber.scheduler.core.util.JsonHelper;
import com.jd.cucumber.scheduler.core.util.LoggerHelper;
import com.jd.cucumber.scheduler.scanner.job.JobDescriptor;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * 自动调度管理器的默认实现
 *
 * @author Xiaolong Zuo
 * @since 0.9.3
 */
public class DefaultAutomaticScheduleManager extends AbstractScheduleManager implements AutomaticScheduleManager {

    private List<JobDescriptor> jobDescriptorList;

    public DefaultAutomaticScheduleManager(JobBeanFactory jobBeanFactory, List<JobDescriptor> jobDescriptorList) {
        this.jobDescriptorList = Collections.unmodifiableList(jobDescriptorList);
        initScheduler(loadProperties());
        JobDataMapManager.initAutomaticScheduler(scheduler, jobBeanFactory);
        initJobDetails();
    }

    protected Properties loadProperties() {
        Properties properties = new Properties();
        try {
            properties.load(ClassHelper.getDefaultClassLoader().getResourceAsStream("quartz.properties"));
        } catch (Exception e) {
            LoggerHelper.warn("quartz properties not found ,use default instead.");
        }
        return properties;
    }

    protected void initJobDetails() {
        for (JobDescriptor descriptor : jobDescriptorList) {
            addJobDetail(new DefaultScheduleJobDescriptor(descriptor));
        }
    }

    protected void addJobDetail(ScheduleJobDescriptor descriptor) {
        try {
            scheduler.addJob(descriptor.jobDetail(), true);
        } catch (SchedulerException e) {
            LoggerHelper.error("add job failed.", e);
            throw new CucumberException(e);
        }
        List<String> jobKeyList = groupNameListMap.get(descriptor.group());
        if (jobKeyList == null) {
            jobKeyList = new ArrayList<>();
        }
        if (!jobKeyList.contains(descriptor.name())) {
            jobKeyList.add(descriptor.name());
        }
        groupNameListMap.put(descriptor.group(), jobKeyList);
        if (!groupList.contains(descriptor.group())) {
            groupList.add(descriptor.group());
        }
    }
    @Override
    public synchronized void startup() {
        getGroupList().forEach(this::startup);
    }
    @Override
    public synchronized void startup(String group) {
        for (String name : getNameList(group)) {
            startup(group, name);
        }
    }

    @Override
    public synchronized void startup(String group, String name) {
        JobKey jobKey = JobKey.jobKey(name, group);
        ScheduleStatus scheduleStatus = jobStatusMap.get(getUniqueId(jobKey));
        if (scheduleStatus == null || scheduleStatus == ScheduleStatus.SHUTDOWN) {
            LoggerHelper.info("job [" + group + "," + name + "] now is shutdown ,begin startup.");
            startupJob(jobKey);
        } else if (scheduleStatus == ScheduleStatus.PAUSE) {
            LoggerHelper.info("job [" + group + "," + name + "] now is pause ,begin resume.");
            resumeJob(jobKey);
        } else {
            LoggerHelper.warn("job [" + group + "," + name + "] has been startup, skip.");
        }
        jobStatusMap.put(getUniqueId(jobKey), ScheduleStatus.STARTUP);
    }

    private void startupJob(JobKey jobKey) {
        ScheduleJobDescriptor jobDescriptor;
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            jobDescriptor = JobDataMapManager.getJobDescriptor(jobDetail);
            if (jobDescriptor.isManualTrigger()) {
                LoggerHelper.error("job need to trigger manual : " + JsonHelper.toJson(jobDescriptor));
                throw new CucumberException(new IllegalArgumentException("job need to trigger manual : " + JsonHelper.toJson(jobDescriptor)));
            }
        } catch (SchedulerException e) {
            LoggerHelper.error("get jobDescriptor [" + jobKey.getGroup() + "," + jobKey.getName() + "] job failed.", e);
            throw new CucumberException(e);
        }
        try {
            scheduler.scheduleJob(jobDescriptor.trigger());
            LoggerHelper.info("job [" + jobKey.getGroup() + "," + jobKey.getName() + "] has been started successfully.");
        } catch (SchedulerException e) {
            LoggerHelper.error("startup [" + jobKey.getGroup() + "," + jobKey.getName() + "] job failed.", e);
            throw new CucumberException(e);
        }
    }

    private void resumeJob(JobKey jobKey) {
        try {
            scheduler.resumeJob(jobKey);
            LoggerHelper.info("job [" + jobKey.getGroup() + "," + jobKey.getName() + "] has been resumed.");
        } catch (SchedulerException e) {
            LoggerHelper.error("resume [" + jobKey.getGroup() + "," + jobKey.getName() + "] job failed.", e);
            throw new CucumberException(e);
        }
    }

}