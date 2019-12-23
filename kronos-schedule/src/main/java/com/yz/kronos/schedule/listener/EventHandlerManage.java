package com.yz.kronos.schedule.listener;

import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;

import java.util.List;

/**
 * @author shanchong
 * @date 2019-12-23
 **/
public interface EventHandlerManage {

    List<ResourceEventHandler<Job>> resourceEventHandlerList();

}
