package com.yz.kronos.config;

import com.yz.kronos.dao.NamespaceRepository;
import com.yz.kronos.enums.YesNoEnum;
import com.yz.kronos.model.NamespaceInfoModel;
import com.yz.kronos.schedule.listener.DefaultEventHandlerManage;
import com.yz.kronos.schedule.listener.EventHandlerManage;
import com.yz.kronos.schedule.listener.JobSynchronzeEventHandler;
import com.yz.kronos.schedule.synchronizer.JobProcessSynchronizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;
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
    private NamespaceRepository namespaceRepository;

    @Bean
    public EventHandlerManage eventHandlerManage(JobProcessSynchronizer jobProcessSynchronizer){
        final DefaultEventHandlerManage defaultEventHandlerManage = new DefaultEventHandlerManage();
        //维护任务分片的同步器
        final JobSynchronzeEventHandler jobSynchronzeEventHandler = new JobSynchronzeEventHandler(jobProcessSynchronizer);
        defaultEventHandlerManage.add(jobSynchronzeEventHandler);
        defaultEventHandlerManage.add(executeLogEventHandler);
        final List<NamespaceInfoModel> namespaceInfoModels = namespaceRepository.findByIsDelete(YesNoEnum.NO.code());
        final List<String> namespaces = namespaceInfoModels.parallelStream().map(NamespaceInfoModel::getNsName).distinct().collect(Collectors.toList());
        defaultEventHandlerManage.setNamespaceList(namespaces);
        return defaultEventHandlerManage;
    }


}
