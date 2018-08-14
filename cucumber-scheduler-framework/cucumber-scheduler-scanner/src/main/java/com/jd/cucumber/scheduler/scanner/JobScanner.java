package com.jd.cucumber.scheduler.scanner;

import com.jd.cucumber.scheduler.scanner.job.JobDescriptor;

import java.util.List;

/**
 * Author xufeng
 * Create Date: 2018/5/23 19:18
 */
public interface JobScanner {
    String APPLICATION_CONTEXT_XML_PATH = "applicationContext.xml";

    /**
     * 获取jobList
     * @return
     */
    List<JobDescriptor> getJobDescriptorList();

    /**
     * 扫描制定类
     * @param className
     */
    void scanClass(String className);

    /**
     * 是否是spring环境
     * @return
     */
    boolean hasSpringEnvironment();
}
