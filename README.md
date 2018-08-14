# cucumber-scheduler


## 可以做什么?
- 支持单节点和多节点运行job
- job转移（如果运行的job所在的节点挂掉，此job会自动转移到其他节点）
- 更方便的控制job的启动和停止
- 支持spring标签，使用方便
- ...

## 如何使用?
- 引入pom,当前稳定版本 

```

    <groupId>com.jd.cucumber.scheduler</groupId>
    <artifactId>cucumber-scheduler-spring</artifactId>
    <version>2.1.1-SNAPSHOT</version>
    
```

-  编写自己的job,参考example

-  启动job,spring 环境下：

```
    <job:job-driven mode="simple"/> ##启动简单的job模式

    <job:job-driven mode="cluster"/> ## 启动集群的job模式
    ## 集群的job只会在某一个节点上运行
    <job:cluster class="com.jd.cucumber.scheduler.samples.jobs.JobOne"/>
    ## 每个节点都会运行
    <job:simple class="com.jd.cucumber.scheduler.samples.jobs.JobOne"/>
```
**注意：** 在xml文件中可能会不识别 job 标签，可以手动添加头文件：

xmlns:job="http://www.jd.cucumber.com/schema/cucumber-job"

http://www.jd.cucumber.com/schema/cucumber-job

http://www.jd.cucumber.com/schema/cucumber-job/cucumber-job-1.0.xsd

-  项目配置

如果启动的是集群job,则需要添加一个 *job.properites* 文件
必须参数：
```
zookeeper.addresses=localhost:2181,localhost:3181,localhost:4181 ## zk集群
project.job.root = test ## 项目在zk集群的node名称，通过此配置可以群分不同的项目或者不同的环境
```

    

# release log
### version 2.1.1 
* 修复任务同时在两台机器上执行的bug

### version 2.1.2
* 性能优化

### version