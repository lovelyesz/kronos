package com.yz.meson.schedule;

import com.yz.meson.model.ExecuteJobInfo;

/**
 * 任务队列管理器
 * @author shanchong
 * @date 2019-11-13
 **/
public interface MesonJobQueueManage extends MesonScheduleSupport {

    void add(String key, ExecuteJobInfo executeJobInfo);

    void remove(String key);
}
