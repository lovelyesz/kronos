package com.yz.kronos.schedule.job;

import com.yz.kronos.KubernetesConfig;
import com.yz.kronos.schedule.queue.JobQueue;

/**
 * 任务关停
 * @author shanchong
 */
public interface JobShutdown {


    /**
     * 任务关停
     * @param execId
     * @param config
     */
    void shutdown(Long execId, KubernetesConfig config);

    /**
     * 任务信息通道
     * @return
     */
    JobQueue jobQueue();

}
