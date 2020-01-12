package com.yz.kronos.schedule.listener;

import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;

import java.util.List;

/**
 * 监听处理管理
 * @author shanchong
 * @date 2019-12-23
 **/
public interface EventHandlerManage {

    /**
     * 监听处理集合
     * @return
     */
    List<ResourceEventHandler<Job>> resourceEventHandlerList();

    /**
     * 命名空间集合
     * @return
     */
    List<String> namespaceList();

}
