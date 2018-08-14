package com.cucumber.scheduler.quartz.bean;

import com.jd.cucumber.scheduler.core.exception.CucumberException;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认的JobBeanFactory实现,用于非spring环境下加载JobBean的实例
 *
 * @author Xiaolong Zuo
 * @since 0.9.3
 */
public class DefaultJobBeanFactory implements JobBeanFactory {

    private ClassLoader classLoader;

    public DefaultJobBeanFactory(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    private Map<String, Object> jobBeanInstanceClassMap = new HashMap<>();

    @Override
    public <T> T getJobBean(String className) {
        T instance = (T) jobBeanInstanceClassMap.get(className);
        if (instance != null) {
            return instance;
        }
        return registerJobBeanInstance(className);
    }

    private synchronized <T> T registerJobBeanInstance(String className) {
        try {
            T instance = (T) jobBeanInstanceClassMap.get(className);
            if (instance == null) {
                Class<T> clazz = (Class<T>) classLoader.loadClass(className);
                instance = clazz.newInstance();
                jobBeanInstanceClassMap.put(className, instance);
            }
            return instance;
        } catch (Exception e) {
            throw new CucumberException(e);
        }
    }

}
