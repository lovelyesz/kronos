package com.yz.kronos.schedule;

import java.util.HashSet;
import java.util.Set;

/**
 * 工作流中断器
 * 缓存正在运行的flow
 * @author shanchong
 * @date 2019-11-21
 **/
public class FlowInterrupter {

    private static FlowInterrupter flowInterrupter;
    private Set<Long> runningFlow = new HashSet<>();

    public static FlowInterrupter getInstance(){
        if (flowInterrupter!=null){
            return flowInterrupter;
        }
        synchronized (FlowInterrupter.class){
            if (flowInterrupter!=null){
                return flowInterrupter;
            }
            flowInterrupter = new FlowInterrupter();
            return flowInterrupter;
        }
    }

    /**
     * 工作流运行过程中判断是否被中断
     * @param flowId
     * @return
     */
    public boolean isActive(Long flowId){
        return !runningFlow.contains(flowId);
    }

    /**
     * 执行完成
     * @param flowId
     */
    public void finish(Long flowId){
        runningFlow.remove(flowId);
    }

    /**
     * 中断
     * @param flowId
     */
    public void interrupter(Long flowId){
        runningFlow.add(flowId);
    }


}
