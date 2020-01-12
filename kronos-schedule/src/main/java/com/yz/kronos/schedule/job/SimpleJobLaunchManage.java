package com.yz.kronos.schedule.job;

import com.yz.kronos.JobInfo;
import com.yz.kronos.KubernetesConfig;
import com.yz.kronos.schedule.handle.StartJobHandle;
import com.yz.kronos.schedule.queue.JobQueue;
import com.yz.kronos.schedule.repository.JobExecuteRepository;

/**
 * 简单的任务执行管理
 * @author shanchong
 * @date 2020-01-12
 **/
public class SimpleJobLaunchManage implements JobLaunchManage {

    JobExecuteRepository jobExecuteRepository;
    JobQueue jobQueue;

    public void setJobExecuteRepository(JobExecuteRepository jobExecuteRepository) {
        this.jobExecuteRepository = jobExecuteRepository;
    }

    public void setJobQueue(JobQueue jobQueue) {
        this.jobQueue = jobQueue;
    }

    /**
     * 调度触发
     *
     * @param flowId
     * @param jobInfo
     * @param config
     * @return execId
     */
    @Override
    public Long schedule(Long flowId, JobInfo jobInfo, KubernetesConfig config) {
        final Integer shareTotal = jobInfo.getShareTotal();
        //记录执行日志
        final Long execId = repository().insert(flowId, jobInfo.getJobId(),shareTotal,jobInfo.getBatchNo());
        jobQueue.add(execId,jobInfo,shareTotal);
        final StartJobHandle startJobHandle = new StartJobHandle(config, jobInfo);
        startJobHandle.startJob(execId.toString());

        return execId;
    }

    /**
     * 任务执行记录库
     *
     * @return
     */
    @Override
    public JobExecuteRepository repository() {
        return jobExecuteRepository;
    }

    /**
     * 任务信息通道
     *
     * @return
     */
    @Override
    public JobQueue jobQueue() {
        return jobQueue;
    }

    /**
     * 任务关停
     *
     * @param execId
     * @param config
     */
    @Override
    public void shutdown(Long execId, KubernetesConfig config) {
        jobQueue().clear(execId);
    }

}
