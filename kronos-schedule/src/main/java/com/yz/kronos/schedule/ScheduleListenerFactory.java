package com.yz.kronos.schedule;

import com.yz.kronos.schedule.config.JobExecutor;

/**
 * 工作流生命周期监听器 init->scheduled->finish  任务初始化->调用k8s->任务执行完成
 * @author shanchong
 * @date 2019-11-12
 **/
public interface ScheduleListenerFactory {

    /**
     * 读取任务后调用
     * @param jobExecutor
     */
    void init(JobExecutor jobExecutor);

    /**
     * 使用任务信息调用k8s后调用
     * @param jobExecutor
     */
    void scheduled(JobExecutor jobExecutor);

    /**
     * 任务执行完成后调用
     * @param jobExecutor
     */
    void finish(JobExecutor jobExecutor);

    /**
     * 强行终止某个任务
     * @param jobExecutor
     */
    void shutdown(JobExecutor jobExecutor);

}
