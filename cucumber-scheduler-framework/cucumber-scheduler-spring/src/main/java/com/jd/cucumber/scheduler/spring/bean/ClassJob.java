package com.jd.cucumber.scheduler.spring.bean;

import com.jd.cucumber.scheduler.core.bean.JobMode;

/**
 * @author xufeng
 * Create Date: 2018/6/7 10:13
 * 扫描spring的jobClass对象
 */
public class ClassJob {
    public ClassJob() {
    }

    public ClassJob(String className, boolean status, JobMode mode) {
        this.className = className;
        this.status = status;
        this.mode = mode;
    }

    private String className;

    private boolean status;

    private JobMode mode;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public JobMode getMode() {
        return mode;
    }

    public void setMode(JobMode mode) {
        this.mode = mode;
    }
}
