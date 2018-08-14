package com.jd.cucumber.scheduler.spring.config;

import com.jd.cucumber.scheduler.spring.application.Bootstrap;
import com.jd.cucumber.scheduler.spring.node.SimpleSpringLocalJobNode;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Objects;

/**
 * spring环境下任务的驱动器,用于启动任务容器
 *
 * @author Xiaolong Zuo
 * @since 0.9.3
 */
public class SpringContextJobDriver implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private String mode;

    private final static String SIMPLE = "simple";
    private final static String CLUSTER = "cluster";

    public void init() throws Exception{
        if (Objects.equals(mode, SIMPLE)) {
            new SimpleSpringLocalJobNode(applicationContext).join();
        }else if (Objects.equals(mode,CLUSTER)){
            Bootstrap.start(applicationContext);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
