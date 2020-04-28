package com.yz.kronos.config;

import com.yz.kronos.dao.FlowInfoRepository;
import com.yz.kronos.schedule.enu.FlowState;
import com.yz.kronos.schedule.model.FlowInfoModel;
import com.yz.kronos.schedule.flow.FlowInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 根据数据库状态判断是否中断
 * @author shanchong
 * @date 2020-01-07
 **/
@Component(value = "flowInterceptor")
public class FlowInterceptorConfig implements FlowInterceptor {

    @Autowired
    private FlowInfoRepository flowInfoRepository;

    /**
     * 是否中断
     *
     * @param flowId
     * @return
     */
    @Override
    public Boolean intercept(Long flowId) {
        final FlowInfoModel flowInfoModel = flowInfoRepository.findById(flowId).orElseGet(FlowInfoModel::new);
        return !FlowState.RUNNING.code().equals(flowInfoModel.getStatus());
    }
}
