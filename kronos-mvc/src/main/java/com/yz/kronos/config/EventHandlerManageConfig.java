package com.yz.kronos.config;

import com.yz.kronos.schedule.listener.DefaultEventHandlerManage;
import com.yz.kronos.schedule.listener.EventHandlerManage;
import com.yz.kronos.schedule.listener.JobSynchronzeEventHandler;
import com.yz.kronos.schedule.synchronizer.JobProcessSynchronizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * k8s任务监听-更新执行记录
 * @author shanchong
 * @date 2019-12-23
 **/
@Configuration
public class EventHandlerManageConfig {

    @Autowired
    private ExecuteLogEventHandler executeLogEventHandler;

    @Bean
    public EventHandlerManage eventHandlerManage(JobProcessSynchronizer jobProcessSynchronizer){
        final DefaultEventHandlerManage defaultEventHandlerManage = new DefaultEventHandlerManage();
        //维护任务分片的同步器
        final JobSynchronzeEventHandler jobSynchronzeEventHandler = new JobSynchronzeEventHandler(jobProcessSynchronizer);
        defaultEventHandlerManage.add(jobSynchronzeEventHandler);
        defaultEventHandlerManage.add(executeLogEventHandler);
        defaultEventHandlerManage.setNamespace("statement");
        return defaultEventHandlerManage;
    }


}
