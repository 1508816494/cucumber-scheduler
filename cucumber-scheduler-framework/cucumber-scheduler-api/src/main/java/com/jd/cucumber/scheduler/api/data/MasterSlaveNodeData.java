package com.jd.cucumber.scheduler.api.data;

import lombok.Getter;
import lombok.Setter;
import org.apache.curator.framework.recipes.cache.ChildData;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MasterSlaveNodeData extends AbstractGenericData<MasterSlaveNodeData, MasterSlaveNodeData.Data> {

    public MasterSlaveNodeData(ChildData childData) {
        super(childData);
    }

    public MasterSlaveNodeData(String path, byte[] bytes) {
        super(path, bytes);
    }

    public MasterSlaveNodeData(String path, Data data) {
        super(path, data);
    }


    public static class Data extends AbstractNodeData<Data> {

        public List<String> getJobPaths() {
            return jobPaths;
        }

        public void setJobPaths(List<String> jobPaths) {
            this.jobPaths = jobPaths;
        }

        private List<String> jobPaths;

        public Data() {
            this(null);
        }

        public Data(String ip) {
            super(ip);
            setNodeState("Slave");
        }

        public void addJobPath(String jobPath) {
            if (jobPaths == null) {
                jobPaths = new ArrayList<>();
            }
            jobPaths.add(jobPath);
            increment();
        }

        public void removeJobPath(String jobPath) {
            if (jobPaths == null) {
                return;
            }
            jobPaths.remove(jobPath);
            decrement();
        }

    }

}
