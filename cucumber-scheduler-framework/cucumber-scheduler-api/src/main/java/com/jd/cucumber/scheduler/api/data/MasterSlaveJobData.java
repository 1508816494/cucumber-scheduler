package com.jd.cucumber.scheduler.api.data;

import lombok.Getter;
import lombok.Setter;
import org.apache.curator.framework.recipes.cache.ChildData;

/**
 * Author xufeng
 * Create Date: 2018/6/7 10:55
 */
@Getter
@Setter
public class MasterSlaveJobData extends AbstractGenericData<MasterSlaveJobData,MasterSlaveJobData.Data> {

    public MasterSlaveJobData(ChildData childData) {
        super(childData);
    }

    public MasterSlaveJobData(String path, byte[] bytes) {
        super(path, bytes);
    }

    public MasterSlaveJobData(String path, Data data) {
        super(path, data);
    }

    @Override
    public boolean equals(Object obj) {
        return getData().equals(obj);
    }

    @Override
    public int hashCode() {

        return getData().getGroupName().hashCode() * 31  + getData().getJobName().hashCode() * 31;
    }

    public static class Data extends AbstractJobData<Data> {
        private String nodePath;


        public String getNodePath() {
            return nodePath;
        }

        public void setNodePath(String nodePath) {
            this.nodePath = nodePath;
        }

        public void clearNodePath() {
            this.nodePath = null;
        }

        @Override
        public void init() {
            super.init();
            setNodePath(null);
        }

        public void release() {
            clearNodePath();
            if (isStartup()) {
                setOriginalJarFileName(getJarFileName());
                setJobOperation("Start");
            }
        }



    }
}
