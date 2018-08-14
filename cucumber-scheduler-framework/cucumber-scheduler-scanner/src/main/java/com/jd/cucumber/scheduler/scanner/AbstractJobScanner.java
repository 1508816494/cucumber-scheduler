package com.jd.cucumber.scheduler.scanner;


import com.jd.cucumber.scheduler.core.util.LoggerHelper;
import com.jd.cucumber.scheduler.scanner.annotation.Disabled;
import com.jd.cucumber.scheduler.scanner.annotation.Schedule;
import com.jd.cucumber.scheduler.scanner.job.JobDescriptor;
import com.jd.cucumber.scheduler.scanner.job.JobDescriptorFactory;
import com.jd.cucumber.scheduler.scanner.job.JobParameter;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Author xufeng
 * Create Date: 2018/5/23 19:33
 */
public abstract class AbstractJobScanner implements JobScanner {
    private List<JobDescriptor> jobDescriptorList;

    private boolean hasSpringEnvironment;

    private ClassLoader classLoader;



    public AbstractJobScanner(ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.hasSpringEnvironment = false;
        this.jobDescriptorList = new ArrayList<>();
    }

    @Override
    public List<JobDescriptor> getJobDescriptorList() {
        return jobDescriptorList;
    }

    @Override
    public boolean hasSpringEnvironment() {
        return hasSpringEnvironment;
    }

    protected void setHasSpringEnvironment(boolean hasSpringEnvironment) {
        this.hasSpringEnvironment = hasSpringEnvironment;
    }


    protected ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public void scanClass(String className) {
        try {
            Class<?> clazz = classLoader.loadClass(className);
            Disabled classDisabled = clazz.getDeclaredAnnotation(Disabled.class);
            if (classDisabled != null) {
                LoggerHelper.debug("skip disabled class [" + className + "]");
                return;
            }
            Method[] methods = clazz.getDeclaredMethods();
            LoggerHelper.debug("scan class [" + className + "]");
            for (Method method : methods) {
                scanMethod(clazz, method);
            }
        } catch (Throwable e) {
            LoggerHelper.debug("scan class [" + className + " : " + e.getClass().getName() + "] failed, has been ignored.", e);
        }
    }

    private void scanMethod(Class<?> clazz, Method method) {
        Schedule schedule = method.getDeclaredAnnotation(Schedule.class);
        Disabled methodDisabled = method.getDeclaredAnnotation(Disabled.class);
        if (methodDisabled != null || schedule == null) {
            LoggerHelper.debug("skip disabled or un-scheduled method [" + clazz.getName() + "." + method.getName() + "]");
            return;
        }
        Type[] parameterTypes = method.getParameterTypes();
        if (parameterTypes != null && parameterTypes.length == 1 && parameterTypes[0] == JobParameter.class) {
            JobDescriptor jobDescriptor = JobDescriptorFactory.jobDescriptor(clazz, method, true, schedule);
            jobDescriptorList.add(jobDescriptor);
            postFindHasParameterJobDescriptor(jobDescriptor);
            LoggerHelper.info("find schedule method [" + clazz.getName() + "." + method.getName() + "(JobParameter)]");
        } else if (parameterTypes == null || parameterTypes.length == 0){
            JobDescriptor jobDescriptor = JobDescriptorFactory.jobDescriptor(clazz, method, false, schedule);
            jobDescriptorList.add(jobDescriptor);
            postFindNotHasParameterJobDescriptor(jobDescriptor);
            LoggerHelper.info("find schedule method [" + clazz.getName() + "." + method.getName() + "]");
        } else {
            LoggerHelper.error("schedule method must not have parameter or have a JobParameter parameter [" + clazz.getName() + "." + method.getName() + "]");
        }
    }

    protected void postFindHasParameterJobDescriptor(JobDescriptor jobDescriptor) {}

    protected void postFindNotHasParameterJobDescriptor(JobDescriptor jobDescriptor) {}

}
