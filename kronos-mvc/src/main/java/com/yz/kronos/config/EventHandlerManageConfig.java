package com.yz.kronos.config;

import com.yz.kronos.dao.NamespaceRepository;
import com.yz.kronos.enums.YesNoEnum;
import com.yz.kronos.model.NamespaceInfoModel;
import com.yz.kronos.schedule.listener.DefaultEventHandlerManage;
import com.yz.kronos.schedule.listener.EventHandlerManage;
import com.yz.kronos.schedule.listener.JobSynchronousEventHandler;
import com.yz.kronos.schedule.synchronizer.JobProcessSynchronizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * k8s任务监听-更新执行记录
 * @author shanchong
 * @date 2019-12-23
 **/
@Configuration
public class EventHandlerManageConfig {

    @Autowired
    private ExecuteLogEventHandler executeLogEventHandler;
    @Autowired
    private JobSynchronousEventHandler jobSynchronousEventHandler;
    @Autowired
    private NamespaceRepository namespaceRepository;

    @Bean
    public EventHandlerManage eventHandlerManage(){
        final DefaultEventHandlerManage defaultEventHandlerManage = new DefaultEventHandlerManage();
        //任务同步器监听
        defaultEventHandlerManage.add(jobSynchronousEventHandler);
        //执行日志监听，更新执行状态
        defaultEventHandlerManage.add(executeLogEventHandler);
        final List<String> namespaces = namespaceRepository.findValidNamespace();
        defaultEventHandlerManage.setNamespaceList(namespaces);
        return defaultEventHandlerManage;
    }


}
