package com.yz.kronos.schedule.job;

import com.yz.kronos.JobInfo;
import com.yz.kronos.KubernetesConfig;
import com.yz.kronos.schedule.queue.JobQueue;
import com.yz.kronos.schedule.repository.JobExecuteRepository;

/**
 * kubernetes Job调度，任务被调度后k8s将异步进行执行，调度执行完成并不是任务执行完成
 * @author shanchong
 */
public interface JobSchedule {

    /**
     * 调度触发
     * @param flowId
     * @param jobInfo
     * @param config
     * @return execId
     */
    Long schedule(Long flowId, JobInfo jobInfo, KubernetesConfig config);

    /**
     * 任务执行记录库
     * @return
     */
    JobExecuteRepository repository();

    /**
     * 任务信息通道
     * @return
     */
    JobQueue jobQueue();

}
