package com.jd.cucumber.scheduler.spring.node;

import com.cucumber.scheduler.quartz.AutomaticScheduleManager;
import com.cucumber.scheduler.quartz.DefaultAutomaticScheduleManager;
import com.cucumber.scheduler.quartz.DefaultManualScheduleManager;
import com.cucumber.scheduler.quartz.ManualScheduleManager;
import com.cucumber.scheduler.quartz.bean.JobBeanFactory;
import com.cucumber.scheduler.quartz.node.AbstractNode;
import com.cucumber.scheduler.quartz.node.Node;
import com.jd.cucumber.scheduler.core.exception.CucumberException;
import com.jd.cucumber.scheduler.core.util.AssertHelper;
import com.jd.cucumber.scheduler.core.util.ClassHelper;
import com.jd.cucumber.scheduler.core.util.JarFileHelper;
import com.jd.cucumber.scheduler.core.util.LoggerHelper;
import com.jd.cucumber.scheduler.scanner.JobScanner;
import com.jd.cucumber.scheduler.scanner.JobScannerFactory;
import com.jd.cucumber.scheduler.scanner.job.JobDescriptor;
import com.jd.cucumber.scheduler.spring.application.Bootstrap;
import com.jd.cucumber.scheduler.spring.bean.SpringJobBeanFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 集群Job节点抽象类,封装了节点状态的切换,子类只需要实现自己加入和退出集群的方法.
 * @author xufeng
 */
public abstract class AbstractClusterJobNode extends AbstractNode implements Node {

    protected AtomicReference<State> state;

    protected AutomaticScheduleManager schedulerManager;


    public AbstractClusterJobNode(ApplicationContext applicationContext) {
        ClassHelper.overrideThreadContextClassLoader(applicationContext.getClassLoader());
        JobBeanFactory jobBeanFactory = new SpringJobBeanFactory(applicationContext);
        JobScanner jobScanner = JobScannerFactory.createClasspathJobScanner(ClassHelper.getDefaultClassLoader());
        this.schedulerManager = new DefaultAutomaticScheduleManager(jobBeanFactory,jobScanner.getJobDescriptorList());
        this.state = new AtomicReference<>();
        this.state.set(State.LATENT);
    }


    protected enum State { LATENT, JOINED, EXITED}

    protected boolean isJoined() {
        return this.state.get() == State.JOINED;
    }

    protected String downloadJarFile(String jarFileName) {
        String jarFilePath;
        try {
            jarFilePath = JarFileHelper.downloadJarFile(Bootstrap.getJobDir(), Bootstrap.getJarUrl(jarFileName));
        } catch (IOException e) {
            LoggerHelper.error("download jar file failed. [" + jarFileName + "]", e);
            throw new CucumberException(e);
        }
        return jarFilePath;
    }

    @Override
    public void join() {
        AssertHelper.isTrue(state.compareAndSet(State.LATENT, State.JOINED), "illegal state .");
        doJoin();
    }

    @Override
    public void exit() {
        AssertHelper.isTrue(state.compareAndSet(State.JOINED, State.EXITED), "illegal state .");
        doExit();
    }

    protected abstract void doJoin();

    protected abstract void doExit();

    /**
     * Master节点抽象监听器.
     * 本抽象类封装了获取Master权限和失去Master权限时线程的挂起控制
     */
    protected abstract class AbstractLeadershipSelectorListener implements LeaderSelectorListener {

        private final AtomicInteger leaderCount = new AtomicInteger();

        private final Object mutex = new Object();
        @Override
        public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
            LoggerHelper.info(getNodeIp() + " is now the leader ,and has been leader " + this.leaderCount.getAndIncrement() + " time(s) before.");
            boolean isJoined = isJoined();
            try {
                if (isJoined) {
                    acquireLeadership();
                }
            } catch (Throwable e) {
                relinquishLeadership();
                LoggerHelper.warn(getNodeIp() + " startup failed,relinquish leadership.", e);
                return;
            }
            try {
                synchronized (mutex) {
                    mutex.wait();
                }
            } catch (InterruptedException e) {
                LoggerHelper.info(getNodeIp() + " has been interrupted.");
            }
        }
        @Override
        public void stateChanged(CuratorFramework client, ConnectionState newState) {
            LoggerHelper.info(getNodeIp() + " state has been changed [" + newState + "]");
            if (!newState.isConnected()) {
                relinquishLeadership();
                synchronized (mutex) {
                    mutex.notify();
                }
            }
        }

        public abstract void acquireLeadership() throws Exception;

        public abstract void relinquishLeadership();

    }

}
