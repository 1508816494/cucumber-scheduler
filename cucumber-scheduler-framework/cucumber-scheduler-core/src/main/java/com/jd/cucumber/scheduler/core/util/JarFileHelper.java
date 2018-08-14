package com.jd.cucumber.scheduler.core.util;

import java.io.File;
import java.io.IOException;

public interface JarFileHelper {

    static String getJarFileName(String jarFilePath) {
        if (jarFilePath == null) {
            return null;
        }
        if (jarFilePath.indexOf("/") < 0 && jarFilePath.indexOf("\\") < 0) {
            return jarFilePath;
        }
        String jarFileName = jarFilePath.substring(jarFilePath.lastIndexOf("/") + 1);
        return jarFileName.substring(jarFileName.lastIndexOf("\\") + 1);
    }

    static String downloadJarFile(String jarFileParentPath, String jarUrl) throws IOException {
        String jarFileName = jarUrl.substring(jarUrl.lastIndexOf("/") + 1);
        String jarFilePath = StringHelper.appendSlant(jarFileParentPath) + jarFileName;
        File file = new File(jarFilePath);
        if (file.exists()) {
            return jarFilePath;
        }
        return HttpHelper.downloadRemoteResource(jarFilePath, jarUrl);
    }

}
