package com.yz.kronos.config;

import com.yz.kronos.dao.FlowInfoRepository;
import com.yz.kronos.enu.FlowState;
import com.yz.kronos.model.FlowInfoModel;
import com.yz.kronos.schedule.intercepter.FlowInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 根据数据库状态判断是否中断
 * @author shanchong
 * @date 2020-01-07
 **/
@Configuration
public class FlowInterceptorConfig {

    @Autowired
    private FlowInfoRepository flowInfoRepository;

    @Bean
    public FlowInterceptor flowInterceptor(){
        return new FlowInterceptor() {
            /**
             * 是否中断
             *
             * @param flowId
             * @return
             */
            @Override
            public Boolean isInterceptor(Long flowId) {
                final FlowInfoModel flowInfoModel = flowInfoRepository.findById(flowId).orElseGet(FlowInfoModel::new);
                return !FlowState.RUNNING.code().equals(flowInfoModel.getStatus());
            }
        };
    }

}
