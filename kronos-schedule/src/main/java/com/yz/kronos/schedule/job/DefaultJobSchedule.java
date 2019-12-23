package com.yz.kronos.schedule.job;

import com.yz.kronos.model.KubernetesConfig;
import com.yz.kronos.schedule.listener.JobScheduleCycle;
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
    JobScheduleCycle jobScheduleCycle;
    JobExecuteRepository repository;

    public DefaultJobSchedule(KubernetesConfig config, JobQueue queue, JobScheduleCycle jobScheduleCycle, JobExecuteRepository repository) {
        this.config = config;
        this.queue = queue;
        this.jobScheduleCycle = jobScheduleCycle;
        this.repository = repository;
    }

    /**
     * k8s配置
     *
     * @return
     */
    @Override
    public KubernetesConfig config() {
        return this.config;
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
     * 任务监听器
     *
     * @return
     */
    @Override
    public JobScheduleCycle cycle() {
        return this.jobScheduleCycle;
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
