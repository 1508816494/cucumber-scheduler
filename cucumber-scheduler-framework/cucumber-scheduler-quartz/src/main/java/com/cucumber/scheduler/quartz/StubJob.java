package com.cucumber.scheduler.quartz;


import com.cucumber.scheduler.quartz.bean.JobBeanFactory;
import com.jd.cucumber.scheduler.core.exception.CucumberException;
import com.jd.cucumber.scheduler.core.util.JsonHelper;
import com.jd.cucumber.scheduler.core.util.LoggerHelper;
import com.jd.cucumber.scheduler.scanner.job.JobDescriptor;
import com.jd.cucumber.scheduler.scanner.job.JobParameter;
import org.quartz.*;

/**
 * 占位任务,它代表着一个由调度器添加的任务,该类会根据调度器传递的参数启动一个任务.
 *
 * @author Xiaolong Zuo
 * @since 0.9.3
 */
public class StubJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        Scheduler scheduler = jobExecutionContext.getScheduler();
        JobDescriptor jobDescriptor = JobDataMapManager.getJobDescriptor(jobDetail);
        JobParameter jobParameter = JobDataMapManager.getJobParameter(jobDetail);
        JobBeanFactory jobBeanFactory = getJobBeanFactory(scheduler, jobDetail);
        String jobMessageString = jobDescriptor + "  JobParameter:" + JsonHelper.toJson(jobParameter);
        try {
            LoggerHelper.info("begin execute job : " + jobMessageString);
            if (jobDescriptor.hasParameter()) {
                jobDescriptor.method().invoke(jobBeanFactory.getJobBean(jobDescriptor.group()), new Object[]{jobParameter});
            } else {
                jobDescriptor.method().invoke(jobBeanFactory.getJobBean(jobDescriptor.group()), new Object[]{});
            }
            LoggerHelper.info("execute job success: " + jobMessageString);
        } catch (Exception e) {
            LoggerHelper.error("execute job failed: " + jobMessageString, e);
            throw new CucumberException(e);
        }
    }

    private JobBeanFactory getJobBeanFactory(Scheduler scheduler, JobDetail jobDetail) {
        ScheduleMode scheduleMode = JobDataMapManager.getScheduleMode(scheduler);
        JobBeanFactory jobBeanFactory;
        if (scheduleMode == ScheduleMode.AUTOMATIC) {
            jobBeanFactory = JobDataMapManager.getJobBeanFactory(scheduler);
        } else if (scheduleMode == ScheduleMode.MANUAL) {
            String jarFilePath = JobDataMapManager.getJarFilePath(jobDetail);
            jobBeanFactory = JobEnvironmentCache.instance().getJobBeanFactory(jarFilePath);
        } else {
            throw new CucumberException(new RuntimeException("Unknown schedule mode."));
        }
        return jobBeanFactory;
    }

}
