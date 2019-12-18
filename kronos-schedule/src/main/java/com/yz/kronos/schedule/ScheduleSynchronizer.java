package com.yz.kronos.schedule;

import java.util.concurrent.TimeUnit;

/**
 * 调度同步器
 * @author shanchong
 * @date 2019-11-12
 **/
public interface ScheduleSynchronizer {

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

}
