package com.yz.kronos.schedule.job;

import com.yz.kronos.schedule.queue.JobQueue;

/**
 * 默认的任务关停
 * @author shanchong
 * @date 2019-12-23
 **/
public class DefaultJobShutdown extends JobShutdown.BaseJobShutdown {

    JobQueue jobQueue;

    public DefaultJobShutdown(JobQueue jobQueue) {
        this.jobQueue = jobQueue;
    }

    /**
     * 任务信息队列
     *
     * @return
     */
    @Override
    public JobQueue queue() {
        return jobQueue;
    }


}
