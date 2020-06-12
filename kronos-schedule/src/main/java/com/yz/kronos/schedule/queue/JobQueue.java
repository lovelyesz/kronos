package com.yz.kronos.schedule.queue;

import com.yz.kronos.JobInfo;
import com.yz.kronos.exception.JobException;

/**
 * 任务信息队列，用于存放任务信息，executor进行消费
 * @author shanchong
 * @date 2019-12-20
 **/
public interface JobQueue {

    String key(Long execId);
    /**
     * 存放任务信息
     * @param execId
     * @param jobInfo 任务信息
     * @param size 数量
     */
    void add(Long execId,JobInfo jobInfo, int size);

    /**
     * 清空队列
     * @param execId
     */
    void clear(Long execId);

    /**
     * 获取队列中的一个元素
     * @param key
     * @return
     * @throws JobException
     */
    JobInfo lpop(String key) throws JobException;

}
