package com.yz.kronos.schedule.listener;

import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单的处理管理
 * @author shanchong
 * @date 2019-12-23
 **/
public class DefaultEventHandlerManage implements EventHandlerManage {

    List<ResourceEventHandler<Job>> resourceEventHandlerList = new ArrayList<>();
    String namespace;

    public void add(ResourceEventHandler<Job> resourceEventHandler){
        resourceEventHandlerList.add(resourceEventHandler);
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public List<ResourceEventHandler<Job>> resourceEventHandlerList() {
        return resourceEventHandlerList;
    }

    @Override
    public String namespace() {
        return namespace;
    }

}
