package com.jd.cucumber.scheduler.samples;

import com.jd.cucumber.scheduler.api.MasterSlaveJobApi;
import com.jd.cucumber.scheduler.api.MasterSlaveNodeApi;
import com.jd.cucumber.scheduler.api.curator.MasterSlaveJobApiImpl;
import com.jd.cucumber.scheduler.api.curator.MasterSlaveNodeApiImpl;
import com.jd.cucumber.scheduler.api.data.MasterSlaveJobData;
import com.jd.cucumber.scheduler.api.data.MasterSlaveNodeData;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

/**
 * Author xufeng
 * Create Date: 2018/6/5 18:18
 */
public class Example {

    private MasterSlaveJobApi masterSlaveJobApi;

    private MasterSlaveNodeApi masterSlaveNodeApi;

    private final String connection = "zk.local:2181";

    private RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, Integer.MAX_VALUE);

    private CuratorFramework client;

    public Example() {
        this.client = CuratorFrameworkFactory.newClient(connection,retryPolicy);
        System.out.println("zk connect...");
        this.client.start();
        System.out.println("zk connect success");
        this.masterSlaveJobApi = new MasterSlaveJobApiImpl(this.client);
        this.masterSlaveNodeApi = new MasterSlaveNodeApiImpl(this.client);
    }

    public MasterSlaveJobApi getMasterSlaveJobApi() {
        return masterSlaveJobApi;
    }

    public MasterSlaveNodeApi getMasterSlaveNodeApi() {
        return masterSlaveNodeApi;
    }

    public static void main(String[] args) {
        Example example = new Example();
        List<MasterSlaveNodeData> nodes = example.getMasterSlaveNodeApi().getAllNodes();
        List<MasterSlaveJobData> jobDatas = example.getMasterSlaveJobApi().getAllJobs();
        for (MasterSlaveNodeData data : nodes) {
            System.out.println(data.toString());
        }
        for (MasterSlaveJobData jobData : jobDatas) {
//            System.out.println(jobData.toString());
//            jobData.getData().setJobOperation("Start");
//            example.getMasterSlaveJobApi().updateJob(jobData.getData().getGroupName(),jobData.getData().getJobName(),jobData.getData());
        }

    }
}
