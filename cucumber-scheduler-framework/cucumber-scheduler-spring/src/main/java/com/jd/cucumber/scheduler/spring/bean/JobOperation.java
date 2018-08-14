package com.jd.cucumber.scheduler.spring.bean;

/**
 * Author xufeng
 * Create Date: 2018/7/4 11:03
 */
public enum JobOperation {

    START("Start"),

    PAUSE("Pause");

    String val;

    JobOperation(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
