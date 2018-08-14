package com.jd.cucumber.scheduler.spring.application;

import com.jd.cucumber.scheduler.core.bean.JobMode;
import com.jd.cucumber.scheduler.core.exception.CucumberException;
import com.jd.cucumber.scheduler.core.util.IOHelper;
import com.jd.cucumber.scheduler.core.util.LoggerHelper;
import com.jd.cucumber.scheduler.core.util.StringHelper;
import com.jd.cucumber.scheduler.scanner.ApplicationClassLoader;
import com.jd.cucumber.scheduler.scanner.ApplicationClassLoaderFactory;
import com.jd.cucumber.scheduler.spring.bean.ClassJob;
import com.jd.cucumber.scheduler.spring.node.MasterSlaveNode;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;


public final class Bootstrap {

    private Bootstrap() {
    }

    private static final ClassLoader SYSTEM_CLASS_LOADER = ClassLoader.getSystemClassLoader();

    private static final String LOG_CONFIG_FILE = "log4j.properties";

    private static final String CUCUMBER_JOB_CONFIG_FILE = "job.properties";

    private static final String QUARTZ_CONFIG_FILE = "quartz.properties";

    private static final String JOB_DIR = "/job";

    private static Properties properties;

    private static MasterSlaveNode masterSlaveNode;

    private static final Map<String, ClassJob> CLASS_JOB_MAP = new HashMap<>(10);

    public static void sendCommand(String command) throws IOException {
        Socket socket = new Socket("localhost", getShutdownPort());
        socket.getOutputStream().write(StringHelper.getBytes(command));
        socket.getOutputStream().flush();
        socket.close();
    }

    /**
     * 监听发送命令
     *
     * @throws IOException
     */
    public static void await() throws IOException {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(getShutdownPort(), 1, InetAddress.getByName("localhost"));
        } catch (Exception e) {
            LoggerHelper.error("socket create failed.", e);
            try {
                stop();
            } catch (Exception e1) {
                LoggerHelper.error("stop node failed.", e1);
                throw new CucumberException(e1);
            }
            throw new CucumberException(e);
        }
        while (true) {
            Socket socket = serverSocket.accept();
            String command = StringHelper.getString(IOHelper.readStreamBytes(socket.getInputStream()));
            if ("Shutdown".equals(command)) {
                break;
            }
        }
    }

    static {
        ApplicationClassLoaderFactory.setSystemClassLoader(SYSTEM_CLASS_LOADER);
        properties = new Properties();
        loadProperties();
    }

    private static void loadProperties() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(QUARTZ_CONFIG_FILE);
            if (inputStream != null) {
                properties.load(inputStream);
            }
            inputStream = classLoader.getResourceAsStream(CUCUMBER_JOB_CONFIG_FILE);
            if (inputStream != null) {
                properties.load(inputStream);
            }
            inputStream = classLoader.getResourceAsStream(LOG_CONFIG_FILE);
            if (inputStream != null) {
                properties.load(inputStream);
            }

        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }

    public static String getJarRepertoryUrl() {
        return StringHelper.appendSlant(properties.getProperty("jar.repertory.url", "http://localhost:8080")) + "job/";
    }

    public static Integer getShutdownPort() {
        return Integer.valueOf(properties.getProperty("shutdown.port", "9101"));
    }

    public static String getZookeeperAddresses() {
        return properties.getProperty("zookeeper.addresses", "localhost:2181");
    }

    public static Properties properties() {
        return new Properties(properties);
    }

    public static String getJarUrl(String jarFileName) {
        return getJarRepertoryUrl() + "masterSlave/" + jarFileName;
    }

    public static String getJobDir() {
        return JOB_DIR + "/masterSlave";
    }

    public static void start(ApplicationContext applicationContext) throws Exception {
        masterSlaveNode = new MasterSlaveNode(applicationContext);
        masterSlaveNode.join();
    }

    public static void stop() throws Exception {
        if (masterSlaveNode != null) {
            masterSlaveNode.exit();
        }
    }

    public static void addClassJob(String clazz, JobMode mode) {
        CLASS_JOB_MAP.putIfAbsent(clazz, new ClassJob(clazz, true, mode));
    }

    public static void updateClassJob(String clazz,boolean status) {
        ClassJob job = CLASS_JOB_MAP.get(clazz);
        if (job != null) {
            job.setStatus(status);
        }
    }

    public static List<ClassJob> getClassJob(JobMode mode) {
        if (CLASS_JOB_MAP.isEmpty()) {
            return Collections.emptyList();
        }
        return CLASS_JOB_MAP.values().stream().filter(item-> item.getMode() == mode).collect(Collectors.toList());
    }

}
