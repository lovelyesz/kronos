package com.yz.kronos.schedule.flow;

import java.util.List;

/**
 * 任务关停
 * @author shanchong
 */
public interface FlowShutdown {

    /**
     * 任务关停
     * @param execIds
     * @param flowId
     */
    void shutdown(List<Long> execIds,Long flowId);


}
