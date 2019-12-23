package com.yz.kronos.schedule.synchronizer;

import java.util.concurrent.TimeUnit;

/**
 * 任务进程同步器
 * @author shanchong
 * @date 2019-12-20
 **/
public interface JobProcessSynchronizer {

    /**
     * 调度同步初始
     * @param key
     * @param count
     */
    void init(String key, int count);

    /**
     * 计数
     * @param key
     */
    void countDown(String key);

    /**
     * 阻塞等待
     * @param key
     * @param time
     * @param unit
     */
    void wait(String key,long time, TimeUnit unit);

    /**
     * 清空同步器
     * @param key
     */
    void delete(String key);

}
