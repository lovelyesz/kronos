package com.yz.kronos.schedule.handle;

/**
 * kubernetes handle Job 处理
 * @author shanchong
 * @date 2019-12-20
 **/
public interface JobHandle {

    /**
     * 开始任务
     * @param execId
     */
    void startJob(String execId);

    /**
     * 停止任务
     */
    void stopJob();

    /**
     * 监听任务
     */
    void listenJob();


}
