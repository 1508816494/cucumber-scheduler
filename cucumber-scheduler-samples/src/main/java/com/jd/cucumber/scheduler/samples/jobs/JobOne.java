package com.jd.cucumber.scheduler.samples.jobs;

import com.jd.cucumber.scheduler.scanner.annotation.Schedule;

/**
 * Author xufeng
 * Create Date: 2018/6/1 11:21
 */
public class JobOne {

    @Schedule(cron = "0/15 * * * * ?")
    public void job5() {
        System.out.println("this is job-one");
    }



    @Schedule(cron = "0/35 * * * * ?")
    public void job3() {
        System.out.println("this is job-three");
    }
}
