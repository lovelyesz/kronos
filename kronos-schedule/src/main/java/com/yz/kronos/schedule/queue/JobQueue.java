package com.yz.kronos.schedule.queue;

/**
 * 任务信息队列，用于存放任务信息，executor进行消费
 * @author shanchong
 * @date 2019-12-20
 **/
public interface JobQueue {

    /**
     * 拉取一个
     * @return
     */
    String lpop(String key);

    /**
     * 存放任务信息
     * @param s 任务信息
     * @param size 数量
     */
    void add(String key,String s,int size);

    /**
     * 存放一个任务信息
     * @param s
     */
    void add(String key,String s);

}
