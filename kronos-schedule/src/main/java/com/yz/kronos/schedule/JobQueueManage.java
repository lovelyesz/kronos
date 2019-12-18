package com.yz.kronos.schedule;

import com.yz.kronos.model.ExecuteJobInfo;

/**
 * 任务队列管理器
 * @author shanchong
 * @date 2019-11-13
 **/
public interface JobQueueManage {

    /**
     * 添加一个任务放到队列中
     * @param key
     * @param executeJobInfo
     */
    void add(String key, ExecuteJobInfo executeJobInfo);

    /**
     * 删除队列
     * @param key
     */
    void remove(String key);
}
