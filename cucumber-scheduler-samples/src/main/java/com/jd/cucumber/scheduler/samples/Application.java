package com.jd.cucumber.scheduler.samples;


import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Author xufeng
 * Create Date: 2018/6/1 11:07
 */
public class Application {

    public static void main(String[] args) throws Exception{
        new ClassPathXmlApplicationContext("applicationContext.xml");
    }
}
