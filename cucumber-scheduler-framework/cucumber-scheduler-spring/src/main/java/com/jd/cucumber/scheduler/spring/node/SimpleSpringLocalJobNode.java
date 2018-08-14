package com.jd.cucumber.scheduler.spring.node;


import com.cucumber.scheduler.quartz.AutomaticScheduleManager;
import com.cucumber.scheduler.quartz.DefaultAutomaticScheduleManager;
import com.cucumber.scheduler.quartz.DefaultScheduleJobDescriptor;
import com.cucumber.scheduler.quartz.bean.JobBeanFactory;
import com.cucumber.scheduler.quartz.node.AbstractNode;
import com.jd.cucumber.scheduler.core.bean.JobMode;
import com.jd.cucumber.scheduler.core.util.ClassHelper;
import com.jd.cucumber.scheduler.core.util.LoggerHelper;
import com.jd.cucumber.scheduler.scanner.JobScanner;
import com.jd.cucumber.scheduler.scanner.JobScannerFactory;
import com.jd.cucumber.scheduler.scanner.job.JobDescriptor;
import com.jd.cucumber.scheduler.spring.application.Bootstrap;
import com.jd.cucumber.scheduler.spring.bean.ClassJob;
import com.jd.cucumber.scheduler.spring.bean.SpringJobBeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * 用于非集群情况下的spring环境.
 * 该节点包含了一个自动调度的管理器,它将会按照任务注解自动的启动所有的任务.
 *
 * @author Xiaolong Zuo
 * @since 0.9.3
 */
public class SimpleSpringLocalJobNode extends AbstractNode {

    private AutomaticScheduleManager schedulerManager;

    private ApplicationContext applicationContext;

    private JobScanner jobScanner;

    public SimpleSpringLocalJobNode(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        ClassHelper.overrideThreadContextClassLoader(applicationContext.getClassLoader());
        jobScanner = JobScannerFactory.createClasspathJobScanner(ClassHelper.getDefaultClassLoader());
    }

    @Override
    public void join() {
        List<ClassJob> list = Bootstrap.getClassJob(JobMode.SIMPLE);
        list.stream().filter(ClassJob::isStatus).forEach(item-> jobScanner.scanClass(item.getClassName()));
        JobBeanFactory jobBeanFactory = new SpringJobBeanFactory(applicationContext);
        this.schedulerManager = new DefaultAutomaticScheduleManager(jobBeanFactory, jobScanner.getJobDescriptorList());
        schedulerManager.startup();
    }

    @Override
    public void exit() {
        schedulerManager.shutdown();
    }

}
