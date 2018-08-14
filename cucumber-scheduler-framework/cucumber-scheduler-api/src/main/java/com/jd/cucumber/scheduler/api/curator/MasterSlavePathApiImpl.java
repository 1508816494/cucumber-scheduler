package com.jd.cucumber.scheduler.api.curator;

import com.jd.cucumber.scheduler.api.MasterSlavePathApi;
import com.jd.cucumber.scheduler.core.exception.CucumberException;
import com.jd.cucumber.scheduler.core.util.Loader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public final class MasterSlavePathApiImpl implements MasterSlavePathApi {

    public static final MasterSlavePathApi INSTANCE = new MasterSlavePathApiImpl();

    private static final Logger logger = LoggerFactory.getLogger(MasterSlavePathApiImpl.class);

    private final String ROOT_PATH = "/job-root";

    private final String CUCUMBER_JOB_CONFIG_FILE = "job.properties";

    private final String PROJECT_JOB_ROOT = "project.job.root";

    private final String MASTER_SLAVE_NODE_PATH;

    private MasterSlavePathApiImpl() {
        Properties properties = new Properties();
        try {

            URL url = Loader.getResource(CUCUMBER_JOB_CONFIG_FILE);
            InputStream inputStream = url.openStream();
            if (inputStream == null) {
                logger.error("url is null");
                throw new CucumberException(new Throwable("url is null"));
            }

            properties.load(inputStream);
            Object o = properties.getProperty(PROJECT_JOB_ROOT);
            if (o == null) {
                logger.error("'project.job.root' property is required");
                throw new CucumberException(new Throwable("'project.job.root' property is required"));
            }
            String projectJobRoot = (String) o;
            MASTER_SLAVE_NODE_PATH = ROOT_PATH + "/" + projectJobRoot;
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }


    @Override
    public String getSelectorPath() {
        return MASTER_SLAVE_NODE_PATH + "/selector";
    }

    @Override
    public String getInitLockPath() {
        return MASTER_SLAVE_NODE_PATH + "/initLock";
    }

    @Override
    public String getNodePath() {
        return MASTER_SLAVE_NODE_PATH + "/nodes/child";
    }

    @Override
    public String getJobPath() {
        return MASTER_SLAVE_NODE_PATH + "/jobs";
    }

}
