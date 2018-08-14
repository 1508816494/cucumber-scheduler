package com.jd.cucumber.scheduler.api;

import com.jd.cucumber.scheduler.api.data.MasterSlaveNodeData;

import java.util.List;

/**
 * 主从模式下Node节点操作API
 *
 * @author Xiaolong Zuo
 * @since 0.9.3
 */
public interface MasterSlaveNodeApi extends NodeApi {

    List<MasterSlaveNodeData> getAllNodes();

    String saveNode(MasterSlaveNodeData.Data data);

    void updateNode(String path, MasterSlaveNodeData.Data data);

    MasterSlaveNodeData getNode(String path);

}
