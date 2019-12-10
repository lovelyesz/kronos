package com.yz.meson.schedule;

import com.yz.meson.schedule.config.MesonJobExecutor;

/**
 * 工作流生命周期监听器 init->scheduled->finish  任务初始化->调用k8s->任务执行完成
 * @author shanchong
 * @date 2019-11-12
 **/
public interface ScheduleListenerFactory extends MesonScheduleSupport {

    /**
     * 读取任务后调用
     * @param jobExecutor
     */
    void init(MesonJobExecutor jobExecutor);

    /**
     * 使用任务信息调用k8s后调用
     * @param jobExecutor
     */
    void scheduled(MesonJobExecutor jobExecutor);

    /**
     * 任务执行完成后调用
     * @param jobExecutor
     */
    void finish(MesonJobExecutor jobExecutor);

    /**
     * 强行终止某个任务
     * @param jobExecutor
     */
    void shutdown(MesonJobExecutor jobExecutor);

}
