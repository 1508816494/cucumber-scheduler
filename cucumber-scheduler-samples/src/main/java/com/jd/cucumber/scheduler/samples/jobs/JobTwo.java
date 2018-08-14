package com.jd.cucumber.scheduler.samples.jobs;

import com.jd.cucumber.scheduler.scanner.annotation.Schedule;

/**
 * Author xufeng
 * Create Date: 2018/6/26 19:31
 */
public class JobTwo {
    @Schedule(cron = "0/25 * * * * ?")
    public void job2() {
        System.out.println("this is job-two");
    }

    @Schedule(cron = "0/25 * * * * ?")
    public void job4() {
        System.out.println("this is job-four");
    }
}
