package com.jd.cucumber.scheduler.scanner;

/**
 * @author xufeng
 * Create Date: 2018/5/23 20:31
 */
public class JobScannerFactory {
    private JobScannerFactory() {}

    public static JobScanner createClasspathJobScanner(ClassLoader classLoader) {
        return new LocalJobScanner(classLoader, false);
    }
}
