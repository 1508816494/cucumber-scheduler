package com.cucumber.scheduler.quartz;

import com.cucumber.scheduler.quartz.bean.JobBeanFactory;
import com.jd.cucumber.scheduler.core.exception.CucumberException;
import com.jd.cucumber.scheduler.core.util.LoggerHelper;
import com.jd.cucumber.scheduler.scanner.job.JobParameter;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;

/**
 * 分装对 jo的操作
 * @author xufeng
 *
 */
public interface JobDataMapManager {

    String JAR_FILE_PATH_KEY = "_jar_file_path";

    String JOB_DESCRIPTOR_KEY = "_job_detail";

    String JOB_PARAMETER_KEY = "_job_parameter";

    String JOB_BEAN_FACTORY_KEY = "_job_bean_factory";

    String SCHEDULE_MODE_KEY = "_schedule_mode_key";

    /**
     * 初始化自动的调度器数据
     *
     * @param scheduler 调度器
     * @param jobBeanFactory JobBean工厂
     */
    static void initAutomaticScheduler(Scheduler scheduler, JobBeanFactory jobBeanFactory) {
        try {
            SchedulerContext schedulerContext = scheduler.getContext();
            schedulerContext.put(JOB_BEAN_FACTORY_KEY, jobBeanFactory);
            schedulerContext.put(SCHEDULE_MODE_KEY, ScheduleMode.AUTOMATIC);
        } catch (SchedulerException e) {
            LoggerHelper.error("get schedule context failed.", e);
            throw new CucumberException(e);
        }
    }

    /**
     * 初始化手动的调度器数据
     *
     * @param scheduler 调度器
     */
    static void initManualScheduler(Scheduler scheduler) {
        try {
            SchedulerContext schedulerContext = scheduler.getContext();
            schedulerContext.put(SCHEDULE_MODE_KEY, ScheduleMode.MANUAL);
        } catch (SchedulerException e) {
            LoggerHelper.error("get schedule context failed.", e);
            throw new CucumberException(e);
        }
    }

    /**
     * 获取调度模式,适用于自动和手动模式
     *
     * @param scheduler 调度器
     * @return 返回当前的调度模式
     */
    static ScheduleMode getScheduleMode(Scheduler scheduler) {
        try {
            return (ScheduleMode) scheduler.getContext().get(SCHEDULE_MODE_KEY);
        } catch (SchedulerException e) {
            throw new CucumberException(e);
        }
    }

    /**
     * 获取任务参数,适用于自动和手动模式
     *
     * @param jobDetail Job信息
     * @return 任务参数
     */
    static JobParameter getJobParameter(JobDetail jobDetail) {
        return (JobParameter) jobDetail.getJobDataMap().get(JOB_PARAMETER_KEY);
    }

    /**
     * 获取可调度的任务描述符,适用于自动和手动模式
     *
     * @param jobDetail Job信息
     * @return 可调度的任务描述符实例
     */
    static ScheduleJobDescriptor getJobDescriptor(JobDetail jobDetail) {
        return (ScheduleJobDescriptor) jobDetail.getJobDataMap().get(JOB_DESCRIPTOR_KEY);
    }

    /**
     * 获取jobBean工厂,适用于自动模式
     *
     * @param scheduler 调度器
     * @return JobBean工厂
     */
    static JobBeanFactory getJobBeanFactory(Scheduler scheduler) {
        try {
            return (JobBeanFactory) scheduler.getContext().get(JOB_BEAN_FACTORY_KEY);
        } catch (SchedulerException e) {
            throw new CucumberException(e);
        }
    }

    /**
     * 获取jar文件路径,适用于手动模式
     *
     * @param jobDetail Job信息
     * @return jar文件路径
     */
    static String getJarFilePath(JobDetail jobDetail) {
        return (String) jobDetail.getJobDataMap().get(JAR_FILE_PATH_KEY);
    }

}
