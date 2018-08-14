package com.jd.cucumber.scheduler.spring.bean;


import com.cucumber.scheduler.quartz.bean.JobBeanFactory;
import com.jd.cucumber.scheduler.core.exception.CucumberException;
import com.jd.cucumber.scheduler.core.util.LoggerHelper;
import com.jd.cucumber.scheduler.scanner.JobScanner;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.ClassUtils;

/**
 * spring环境下的JobBean工厂实现,所有的JobBean优先从spring IOC容器中获取.
 *
 * @author Xiaolong Zuo
 * @since 0.9.3
 */
public class SpringJobBeanFactory implements JobBeanFactory {

    private ApplicationContext applicationContext;

    private ClassLoader classLoader;

    /**
     * 该构造函数用于集群环境
     *
     * @throws BeansException
     */
    public SpringJobBeanFactory(ClassLoader classLoader) throws BeansException {
        this.classLoader = classLoader;
        ClassUtils.overrideThreadContextClassLoader(classLoader);
        this.applicationContext = new ClassPathXmlApplicationContext(JobScanner.APPLICATION_CONTEXT_XML_PATH);
    }

    /**
     * 该构造函数用于非集群环境
     *
     * @param applicationContext 本地的ApplicationContext上下文对象
     */
    public SpringJobBeanFactory(ApplicationContext applicationContext) {
        this.classLoader = applicationContext.getClassLoader();
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> T getJobBean(String className) {
        T instance;
        Class<T> clazz;
        try {
            clazz = (Class<T>) classLoader.loadClass(className);
        } catch (Throwable e) {
            throw new CucumberException(e);
        }
        try {
            instance = applicationContext.getBean(clazz);
        } catch (Throwable e) {
            LoggerHelper.warn("can't find instance for " + className);
            try {
                instance = clazz.newInstance();
            } catch (Throwable e1) {
                throw new CucumberException(e1);
            }
        }
        return instance;
    }

}
