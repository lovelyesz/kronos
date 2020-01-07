package com.yz.kronos.schedule.intercepter;

/**
 * 工作流中断器
 * @author shanchong
 */
public interface FlowInterceptor {

    /**
     * 是否中断
     * @param flowId
     * @return
     */
    Boolean isInterceptor(Long flowId);

    class DefaultFlowInterceptor implements FlowInterceptor{

        @Override
        public Boolean isInterceptor(Long flowId) {
            return false;
        }

    }
}
