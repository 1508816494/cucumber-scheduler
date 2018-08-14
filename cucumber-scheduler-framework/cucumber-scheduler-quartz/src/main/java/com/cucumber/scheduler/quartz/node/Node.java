package com.cucumber.scheduler.quartz.node;

/**
 * 该接口代表一个可以执行任务的节点.
 *
 * @author Xiaolong Zuo
 * @since 0.9.3
 */
public interface Node {

    void join();

    void exit();

}
