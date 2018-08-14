package com.jd.cucumber.scheduler.api.util;

import com.jd.cucumber.scheduler.core.util.AssertHelper;

/**
 * path帮助类
 *
 * @author Xiaolong Zuo
 * @since 0.9.3
 */
public interface PathHelper {

    static String getParentPath(String path) {
        AssertHelper.notNull(path, "path can't be null.");
        int index = path.lastIndexOf("/");
        if (index < 0) {
            return path;
        }
        return path.substring(0, index);
    }

    static String getJobPath(String jobParentPath, String group, String name) {
        AssertHelper.notNull(jobParentPath, "jobParentPath can't be null.");
        AssertHelper.notNull(group, "group can't be null.");
        AssertHelper.notNull(name, "name can't be null.");
        return jobParentPath + "/" + group + "." + name;
    }

    static String getEndPath(String path) {
        AssertHelper.notNull(path, "path can't be null.");
        return path.contains("/") ? path.substring(path.lastIndexOf("/") + 1) : path;
    }

}
