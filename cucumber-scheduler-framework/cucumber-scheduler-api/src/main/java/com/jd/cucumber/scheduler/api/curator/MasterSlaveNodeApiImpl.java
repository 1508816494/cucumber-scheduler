

package com.jd.cucumber.scheduler.api.curator;

import com.jd.cucumber.scheduler.api.MasterSlaveNodeApi;
import com.jd.cucumber.scheduler.api.data.MasterSlaveNodeData;
import com.jd.cucumber.scheduler.api.util.PathHelper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Xiaolong Zuo
 * @since 0.9.3
 */
public class MasterSlaveNodeApiImpl extends AbstractCurdApiImpl implements MasterSlaveNodeApi {

    public MasterSlaveNodeApiImpl(CuratorFramework client) {
        super(client);
    }

    @Override
    public List<MasterSlaveNodeData> getAllNodes() {
        List<ChildData> childDataList = getChildren(PathHelper.getParentPath(getMasterSlavePathApi().getNodePath()));
        return childDataList.stream().map(MasterSlaveNodeData::new).collect(Collectors.toList());
    }

    @Override
    public String saveNode(MasterSlaveNodeData.Data data) {
        MasterSlaveNodeData masterSlaveNodeData = new MasterSlaveNodeData(getMasterSlavePathApi().getNodePath(), data);
        return createEphemeralSequential(masterSlaveNodeData.getPath(), masterSlaveNodeData.getDataBytes());
    }

    @Override
    public void updateNode(String path, MasterSlaveNodeData.Data data) {
        MasterSlaveNodeData masterSlaveNodeData = new MasterSlaveNodeData(path, data);
        setData(masterSlaveNodeData.getPath(), masterSlaveNodeData.getDataBytes());
    }

    @Override
    public MasterSlaveNodeData getNode(String path) {
        return new MasterSlaveNodeData(getData(path));
    }


    @Override
    public void deleteNode(String path) {
        delete(path);
    }

}
