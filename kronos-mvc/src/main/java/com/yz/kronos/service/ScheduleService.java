package com.yz.kronos.service;

import com.alibaba.fastjson.JSONObject;
import com.yz.kronos.dao.FlowInfoRepository;
import com.yz.kronos.dao.JobInfoRepository;
import com.yz.kronos.dao.JobRelationRepository;
import com.yz.kronos.dao.NamespaceRepository;
import com.yz.kronos.enu.FlowState;
import com.yz.kronos.enu.JobState;
import com.yz.kronos.model.*;
import com.yz.kronos.schedule.config.JobExecutor;
import com.yz.kronos.schedule.enu.ImagePillPolicy;
import com.yz.kronos.schedule.flow.FlowInfo;
import com.yz.kronos.schedule.flow.FlowSchedule;
import com.yz.kronos.schedule.job.JobInfo;
import com.yz.kronos.util.ExecuteUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shanchong
 * @date 2019-10-28
 **/
@Slf4j
@Service
@EnableAsync
public class ScheduleService {

    @Autowired
    private KubernetesConfig kubernetesConfig;
    @Autowired
    private FlowInfoRepository flowInfoRepository;
    @Autowired
    private JobInfoRepository jobInfoRepository;
    @Autowired
    private NamespaceRepository namespaceRepository;
    @Autowired
    private JobRelationRepository jobRelationRepository;
    @Autowired
    private FlowSchedule flowSchedule;


    /**
     * 执行工作流
     * JobA*1->JobB*10->JobC*1 现只支持这种无杈结构
     * @param flowId
     */
    @Async
    public void runFlow(Long flowId) {
        FlowInfoModel flowInfoModel = flowInfoRepository.findById(flowId).get();
        if (!FlowState.RUNNABLE.code().equals(flowInfoModel.getStatus())) {
            log.error("kronos flow execute fail , flow {} status is {}", flowId, flowInfoModel.getStatus());
            return;
        }
        flowInfoModel.setStatus(FlowState.RUNNING.code());
        flowInfoRepository.save(flowInfoModel);
        Long namespaceId = flowInfoModel.getNamespaceId();
        NamespaceInfoModel namespaceInfoModel = namespaceRepository.findById(namespaceId).get();
        //任务和工作流关联关系
        List<JobRelationModel> jobRelationModelList = jobRelationRepository.findByFlowId(flowId);
        List<Long> jobIds = jobRelationModelList.parallelStream().map(JobRelationModel::getJobId).collect(Collectors.toList());
        //任务信息
        List<JobInfoModel> jobInfoModelList = jobInfoRepository.findByIdIn(jobIds);
        Map<Long, JobInfoModel> jobInfoModelMap = jobInfoModelList.parallelStream().collect(Collectors.toMap(JobInfoModel::getId, p -> p));

        List<FlowInfo.FlowElement> jobInfoList = new ArrayList<>();
        for (int i = 0; i < jobRelationModelList.size(); i++) {
            final JobRelationModel e = jobRelationModelList.get(i);
            final FlowInfo.FlowElement flowElement = new FlowInfo.FlowElement();
            final JobInfo jobInfo = new JobInfo();
            final Long jobId = e.getJobId();
            final JobInfoModel jobInfoModel = jobInfoModelMap.get(jobId);
            jobInfo.setClazz(jobInfoModel.getClazz());
            jobInfo.setIndex(i);
            jobInfo.setShareTotal(e.getShareTotal());
            jobInfo.setResources(JSONObject.parseObject(jobInfoModel.getResources()));
            final JobInfo.NamespaceInfo namespaceInfo = new JobInfo.NamespaceInfo();
            namespaceInfo.setCmd(namespaceInfoModel.getCmd());
            namespaceInfo.setImage(namespaceInfoModel.getImage());
            namespaceInfo.setName(namespaceInfoModel.getNsName());
            jobInfo.setNamespace(namespaceInfo);
            flowElement.setJobInfo(jobInfo);
            flowElement.setSort(e.getSort());
            flowElement.setJobId(jobId);
            jobInfoList.add(flowElement);
        }
        kubernetesConfig.setImagePullPolicy(ImagePillPolicy.Always.name());

        final FlowInfo flowInfo = new FlowInfo();
        flowInfo.setJobInfoList(jobInfoList);
        flowInfo.setFlowId(flowId);
        flowSchedule.schedule(flowInfo);

        flowInfoModel.setStatus(FlowState.RUNNABLE.code());
        flowInfoRepository.save(flowInfoModel);
    }

    @Async
    public void stopFlow(Long flowId){
//        FlowInfoModel flowInfoModel = flowInfoRepository.findById(flowId).get();
//        log.warn("kronos flow is stopping flow:{}",flowId);
//        final Integer state = flowInfoModel.getStatus();
//        if (!FlowState.RUNNING.code().equals(state)){
//            log.error("kronos flow stop fail , flow {} status is {}", flowId, flowInfoModel.getStatus());
//            return;
//        }
//        List<ExecuteLogModel> executeLogModelList = executeLogService.findByFlowIdAndState(flowId, JobState.SCHEDULED,JobState.INIT);
//        if (executeLogModelList.isEmpty()){
//            log.error("kronos flow not find status is 0 or 1 of execute log flowId:{}",flowId);
//            return;
//        }
//
//        FlowScheduleFactory flowScheduleManager = new FlowScheduleFactory(redisScheduleSynchronizer,kubernetesConfig);
//        List<JobExecutor> jobExecutorList = executeLogModelList.stream().map(executeLogModel -> {
//            JobExecutor jobExecutor = new JobExecutor();
//            jobExecutor.setExecLogId(executeLogModel.getId());
//            jobExecutor.setFlowId(executeLogModel.getFlowId());
//            jobExecutor.setJobId(executeLogModel.getJobId());
//            return jobExecutor;
//        }).collect(Collectors.toList());
//        flowScheduleManager.shutdown(flowId, jobExecutorList,flowScheduleListener);
//        executeLogModelList.forEach(executeLogModel->{
//            executeLogService.update(executeLogModel.getId(),JobState.SHUTDOWN);
//        });
//        flowInfoModel.setStatus(FlowState.RUNNABLE.code());
//        //更新工作流状态
//        flowInfoRepository.save(flowInfoModel);
//        log.warn("kronos flow is stopped flow:{}",flowId);
    }

}
