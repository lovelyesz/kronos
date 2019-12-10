package com.yz.meson.service;

import com.alibaba.fastjson.JSONObject;
import com.yz.meson.config.FlowScheduleListenerFactory;
import com.yz.meson.config.KubernetesConfig;
import com.yz.meson.config.RedisScheduleSynchronizer;
import com.yz.meson.dao.FlowInfoRepository;
import com.yz.meson.dao.JobInfoRepository;
import com.yz.meson.dao.JobRelationRepository;
import com.yz.meson.dao.NamespaceRepository;
import com.yz.meson.enu.FlowState;
import com.yz.meson.enu.JobState;
import com.yz.meson.model.*;
import com.yz.meson.schedule.FlowScheduleFactory;
import com.yz.meson.schedule.config.MesonJobExecutor;
import com.yz.meson.schedule.enu.ImagePillPolicy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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
    private FlowScheduleListenerFactory flowScheduleListener;
    @Autowired
    private RedisScheduleSynchronizer redisScheduleSynchronizer;
    @Autowired
    private ExecuteLogService executeLogService;


    /**
     * 执行工作流
     * JobA*1->JobB*10->JobC*1 现只支持这种无杈结构
     * @param flowId
     */
    @Async
    public void runFlow(Long flowId) {
        FlowInfoModel flowInfoModel = flowInfoRepository.findById(flowId).get();
        if (!FlowState.RUNNABLE.code().equals(flowInfoModel.getState())) {
            log.error("meson flow execute fail , flow {} state is {}", flowId, flowInfoModel.getState());
            return;
        }
        flowInfoModel.setState(FlowState.RUNNING.code());
        flowInfoRepository.save(flowInfoModel);
        Long namespaceId = flowInfoModel.getNamespaceId();
        NamespaceInfoModel namespaceInfoModel = namespaceRepository.findById(namespaceId).get();
        //任务和工作流关联关系
        List<JobRelationModel> jobRelationModelList = jobRelationRepository.findByFlowId(flowId);
        List<Long> jobIds = jobRelationModelList.parallelStream().map(JobRelationModel::getJobId).collect(Collectors.toList());
        //任务信息
        List<JobInfoModel> jobInfoModelList = jobInfoRepository.findByIdIn(jobIds);
        Map<Long, JobInfoModel> jobInfoModelMap = jobInfoModelList.parallelStream().collect(Collectors.toMap(JobInfoModel::getId, p -> p));

        List<MesonJobExecutor> jobExecutorList = jobRelationModelList.stream()
                .sorted(Comparator.comparing(JobRelationModel::getSort))
                .map(e -> {
                    Long jobId = e.getJobId();
                    JobInfoModel jobInfoModel = jobInfoModelMap.get(jobId);
                    final Integer shareTotal = e.getShareTotal();
                    MesonJobExecutor mesonJobExecutor = buildExecutor(flowId, namespaceInfoModel, jobInfoModel,shareTotal);
                    mesonJobExecutor.setSort(e.getSort());
                    return mesonJobExecutor;
                }).collect(Collectors.toList());
        kubernetesConfig.setImagePullPolicy(ImagePillPolicy.Always.name());
        FlowScheduleFactory flowScheduleManager =
                new FlowScheduleFactory(redisScheduleSynchronizer,kubernetesConfig);

        flowScheduleManager.execute(flowId,jobExecutorList,flowScheduleListener);
        flowInfoModel.setState(FlowState.RUNNABLE.code());
        flowInfoRepository.save(flowInfoModel);
    }

    private MesonJobExecutor buildExecutor(Long flowId, NamespaceInfoModel namespaceInfoModel,JobInfoModel jobInfoModel,Integer shareTotal) {
        MesonJobExecutor jobExecutor = new MesonJobExecutor();
        jobExecutor.setNamespace(namespaceInfoModel.getNsName());
        jobExecutor.setCmd(namespaceInfoModel.getCmd());
        jobExecutor.setImage(namespaceInfoModel.getImage());
        jobExecutor.setClazz(jobInfoModel.getClazz());
        jobExecutor.setShareCount(shareTotal);
        jobExecutor.setJobId(jobInfoModel.getId());
        jobExecutor.setFlowId(flowId);
        String resources = jobInfoModel.getResources();
        if (resources!=null&&!"".equals(resources)){
            JSONObject jsonObject = JSONObject.parseObject(resources);
            jobExecutor.setResources(jsonObject.entrySet().parallelStream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e1 -> e1.getValue().toString())));
        }
        return jobExecutor;
    }

    @Async
    public void stopFlow(Long flowId){
        FlowInfoModel flowInfoModel = flowInfoRepository.findById(flowId).get();
        log.warn("meson flow is stopping flow:{}",flowId);
        final Integer state = flowInfoModel.getState();
        if (!FlowState.RUNNING.code().equals(state)){
            log.error("meson flow stop fail , flow {} state is {}", flowId, flowInfoModel.getState());
            return;
        }
        List<ExecuteLogModel> executeLogModelList = executeLogService.findByFlowIdAndState(flowId, JobState.SCHEDULED,JobState.INIT);
        if (executeLogModelList.isEmpty()){
            log.error("meson flow not find state is 0 or 1 of execute log flowId:{}",flowId);
            return;
        }

        FlowScheduleFactory flowScheduleManager = new FlowScheduleFactory(redisScheduleSynchronizer,kubernetesConfig);
        List<MesonJobExecutor> mesonJobExecutorList = executeLogModelList.stream().map(executeLogModel -> {
            MesonJobExecutor jobExecutor = new MesonJobExecutor();
            jobExecutor.setExecLogId(executeLogModel.getId());
            jobExecutor.setFlowId(executeLogModel.getFlowId());
            jobExecutor.setJobId(executeLogModel.getJobId());
            return jobExecutor;
        }).collect(Collectors.toList());
        flowScheduleManager.shutdown(flowId,mesonJobExecutorList,flowScheduleListener);
        executeLogModelList.forEach(executeLogModel->{
            executeLogService.update(executeLogModel.getId(),JobState.SHUTDOWN);
        });
        flowInfoModel.setState(FlowState.RUNNABLE.code());
        //更新工作流状态
        flowInfoRepository.save(flowInfoModel);
        log.warn("meson flow is stopped flow:{}",flowId);
    }

}
