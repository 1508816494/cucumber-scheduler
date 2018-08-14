package com.jd.cucumber.scheduler.scanner;

import com.jd.cucumber.scheduler.core.exception.ConfigException;
import com.jd.cucumber.scheduler.core.util.ClassHelper;
import com.jd.cucumber.scheduler.core.util.LoggerHelper;
import com.jd.cucumber.scheduler.core.util.StringHelper;
import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Author xufeng
 * Create Date: 2018/5/23 19:33
 */
public class LocalJobScanner extends AbstractJobScanner{
    /**
     * 扫描器唯一的构造方法,将使用指定的类加载器去扫描
     * @param classLoader 扫描器所使用的类加载器
     * @param containsClasspath 是否要扫描classpath路径下的类
     */
    LocalJobScanner(ClassLoader classLoader, boolean containsClasspath) {
        super(classLoader);
        if (containsClasspath) {
            scanClasspath();
        }
    }

    protected void scanClasspath() {
        URL url = getClassLoader().getResource("");
        if (url == null) {
            LoggerHelper.error("classpath can't be find.");
            throw new ConfigException();
        }
        if (url.getProtocol().toLowerCase().equals("file")) {
            LoggerHelper.info("scan classpath [" + url + "]");
            File[] children = new File(url.getFile()).listFiles();
            if (children != null && children.length > 0) {
                for (File child : children) {
                    fill("", child);
                }
            }
        } else {
            LoggerHelper.warn("url [" + url + "] is not a file but a " + url.getProtocol() + ".");
        }
    }


    private void fill(String packageName, File file) {
        String fileName = file.getName();
        if (file.isFile() && fileName.endsWith(".class")) {
            String className = packageName + "." + fileName.substring(0, fileName.lastIndexOf("."));
            super.scanClass(className);
        } else if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null && children.length > 0) {
                for (File child : children) {
                    if (StringHelper.isEmpty(packageName)) {
                        fill(fileName, child);
                    } else {
                        fill(packageName + "." + fileName, child);
                    }
                }
            }
        }
    }


}
