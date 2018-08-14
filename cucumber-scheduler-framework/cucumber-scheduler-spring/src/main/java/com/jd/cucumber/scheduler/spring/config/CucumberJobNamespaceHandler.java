package com.jd.cucumber.scheduler.spring.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 命名空间处理器
 *
 * @author Xiaolong Zuo
 * @since 0.9.3
 */
public class CucumberJobNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        JobDrivenBeanDefinitionParser parser = new JobDrivenBeanDefinitionParser();
        registerBeanDefinitionParser("job-driven", parser);
        registerBeanDefinitionParser("cluster", parser);
        registerBeanDefinitionParser("simple", parser);
    }

}
