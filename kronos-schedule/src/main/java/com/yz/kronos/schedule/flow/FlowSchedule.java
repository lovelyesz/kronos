package com.yz.kronos.schedule.flow;

import com.yz.kronos.schedule.model.FlowInfo;

/**
 * 工作流调度
 * @author shanchong
 */
public interface FlowSchedule {

    /**
     * 工作流执行调度
     * @param flowInfo
     */
    void schedule(FlowInfo flowInfo);


}
