package com.yz.kronos.schedule.job;

import com.yz.kronos.KubernetesConfig;
import com.yz.kronos.schedule.queue.JobQueue;
import com.yz.kronos.schedule.repository.JobExecuteRepository;

/**
 * 简单的任务触发器
 * @author shanchong
 * @date 2019-12-20
 **/
public class DefaultJobSchedule extends JobSchedule.BaseJobSchedule {

    KubernetesConfig config;
    JobQueue queue;
    JobExecuteRepository repository;

    public DefaultJobSchedule(KubernetesConfig config, JobQueue queue, JobExecuteRepository repository) {
        this.config = config;
        this.queue = queue;
        this.repository = repository;
    }

    /**
     * 任务信息队列
     *
     * @return
     */
    @Override
    public JobQueue queue() {
        return queue;
    }

    /**
     * 任务执行记录库
     *
     * @return
     */
    @Override
    public JobExecuteRepository repository() {
        return repository;
    }

}
