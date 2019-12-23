package com.yz.kronos.schedule.listener;

/**
 * 任务监听器
 * @author shanchong
 */
public interface JobScheduleCycle {

    /**
     * 任务开始
     */
    void startJob();

    /**
     * 任务调用完成
     */
    void processJob();

}
