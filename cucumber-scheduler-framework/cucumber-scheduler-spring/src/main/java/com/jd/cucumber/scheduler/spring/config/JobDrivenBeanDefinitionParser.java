
package com.jd.cucumber.scheduler.spring.config;

import com.jd.cucumber.scheduler.core.bean.JobMode;
import com.jd.cucumber.scheduler.spring.application.Bootstrap;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.Objects;

/**
 * 解析job-driven标签
 *
 * @author Xiaolong Zuo
 * @since 0.9.3
 */
public class JobDrivenBeanDefinitionParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String tagName = element.getTagName();
        if (Objects.equals(tagName,"job:cluster")) {
            return handleJobTag(element,"CLUSTER");
        }else if(Objects.equals(tagName,"job:simple")){
            return handleJobTag(element,"SIMPLE");
        } else {
            return handleJobDrivenTag(element,parserContext);
        }
    }

    /**
     * 处理job-driven 标签
     * @param element
     * @param parserContext
     * @return
     */
    private BeanDefinition handleJobDrivenTag(Element element, ParserContext parserContext) {
        AbstractBeanDefinition beanDefinition = new GenericBeanDefinition();
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        beanDefinition.setBeanClass(SpringContextJobDriver.class);
        propertyValues.addPropertyValue("mode", element.getAttribute("mode"));
        beanDefinition.setPropertyValues(propertyValues);
        beanDefinition.setInitMethodName("init");
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, parserContext.getRegistry());
        return beanDefinition;
    }


    private BeanDefinition handleJobTag(Element element,String mode) {
        Bootstrap.addClassJob(element.getAttribute("class"), JobMode.valueOf(mode));
        return new GenericBeanDefinition();
    }

}
