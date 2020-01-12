package com.yz.kronos.schedule.flow;

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
    Boolean intercept(Long flowId);

    class DefaultFlowInterceptor implements FlowInterceptor{

        @Override
        public Boolean intercept(Long flowId) {
            return false;
        }

    }
}
