package com.yz.kronos.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.yz.kronos.CallResultConstant;
import com.yz.kronos.JobInfo;
import com.yz.kronos.KubernetesConfig;
import com.yz.kronos.dao.FlowInfoRepository;
import com.yz.kronos.dao.JobInfoRepository;
import com.yz.kronos.dao.JobRelationRepository;
import com.yz.kronos.dao.NamespaceRepository;
import com.yz.kronos.enu.FlowState;
import com.yz.kronos.enu.JobState;
import com.yz.kronos.enums.YesNoEnum;
import com.yz.kronos.model.*;
import com.yz.kronos.schedule.enu.ImagePillPolicy;
import com.yz.kronos.schedule.flow.AbstractFlowManage;
import com.yz.kronos.schedule.flow.FlowInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shanchong
 * @date 2019-11-07
 **/
@Slf4j
@Service
public class FlowInfoServiceImpl implements FlowInfoService {

    @Autowired
    FlowInfoRepository flowInfoRepository;
    @Autowired
    JobRelationRepository jobRelationRepository;
    @Autowired
    JobInfoRepository jobInfoRepository;
    @Autowired
    private NamespaceRepository namespaceRepository;
    @Autowired
    private AbstractFlowManage flowManage;
    @Autowired
    private KubernetesConfig kubernetesConfig;
    @Autowired
    private ExecuteLogService executeLogService;

    @Override
    public FlowInfoModel get(Long id){
        return flowInfoRepository.findById(id).get();
    }

    @Override
    public FlowInfoModel save(FlowInfoModel flowInfoModel){
        return flowInfoRepository.save(flowInfoModel);
    }

    @Override
    public void delete(Long id){
        final FlowInfoModel flowInfoModel = flowInfoRepository.findById(id).get();
        flowInfoModel.setIsDelete(YesNoEnum.YES.code());
        flowInfoRepository.save(flowInfoModel);
    }

    @Async
    @Override
    public void schedule(Long flowId){
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
        flowManage.schedule(flowInfo);

        flowInfoModel.setStatus(FlowState.RUNNABLE.code());
        flowInfoRepository.save(flowInfoModel);
    }

    @Async
    @Override
    public void shutdown(Long flowId) {
        FlowInfoModel flowInfoModel = flowInfoRepository.findById(flowId).get();
        log.warn("kronos flow is stopping flow:{}",flowId);
        final Integer state = flowInfoModel.getStatus();
        if (!FlowState.RUNNING.code().equals(state)){
            log.error("kronos flow stop fail , flow {} status is {}", flowId, flowInfoModel.getStatus());
            return;
        }
        List<ExecuteLogModel> executeLogModelList = executeLogService.findByFlowIdAndState(flowId, JobState.RUNNING,JobState.INIT);
        if (executeLogModelList.isEmpty()){
            log.error("kronos flow not find status is 0 or 1 of execute log flowId:{}",flowId);
            return;
        }
        final List<String> execIds = executeLogModelList.parallelStream().map(e->e.getId().toString())
                .collect(Collectors.toList());
        flowManage.shutdown(execIds);

        executeLogModelList.forEach(executeLogModel->{
            executeLogService.update(executeLogModel.getId(),JobState.SHUTDOWN);
        });
        flowInfoModel.setStatus(FlowState.RUNNABLE.code());
        //更新工作流状态
        flowInfoRepository.save(flowInfoModel);

    }

    @Override
    public List<FlowInfoModel> selectByIds(Set<Long> flowIds) {
        return flowInfoRepository.findByIdIn(flowIds);
    }

    @Override
    public List<FlowInfoModel> selectByFlowName(String flowName) {
        return flowInfoRepository.findByFlowNameLike(flowName);
    }

    @Override
    public PageResult<FlowInfoModel> page(Long namespaceId, Integer page, Integer limit) {
        final FlowInfoModel flowInfoModel = new FlowInfoModel();
        flowInfoModel.setNamespaceId(namespaceId);
        flowInfoModel.setIsDelete(YesNoEnum.NO.code());
        final Page<FlowInfoModel> flowInfoModelPage = flowInfoRepository.findAll(Example.of(flowInfoModel), PageRequest.of(page, limit,Sort.by("id")));
        final Set<Long> flowIds = flowInfoModelPage.map(FlowInfoModel::getId).stream().collect(Collectors.toSet());
        final List<JobRelationModel> relationModels = jobRelationRepository.findByFlowIdIn(flowIds);

        final Set<Long> jobIds = relationModels.parallelStream().map(JobRelationModel::getJobId).collect(Collectors.toSet());
        final List<JobInfoModel> jobInfoModels = jobInfoRepository.findByIdIn(jobIds);
        final Map<Long, JobInfoModel> jobInfoModelMap = jobInfoModels.parallelStream()
                .collect(Collectors.toMap(JobInfoModel::getId, p -> p));
        final Map<Long, List<JobInfoModel>> flowIdJobMap = relationModels.stream().map(e -> {
            final Long jobId = e.getJobId();
            final JobInfoModel jobInfoModel = jobInfoModelMap.get(jobId);
            jobInfoModel.setShareTotal(e.getShareTotal());
            jobInfoModel.setSort(e.getSort());
            jobInfoModel.setFlowId(e.getFlowId());
            return jobInfoModel;
        }).sorted(Comparator.comparing(JobInfoModel::getSort))
                .collect(Collectors.groupingBy(JobInfoModel::getFlowId, Collectors.toList()));
        flowInfoModelPage.forEach(e->{
            e.setJobList(flowIdJobMap.getOrDefault(e.getId(), Lists.newArrayList()));
        });
        return PageResult.<FlowInfoModel>builder()
                .code(CallResultConstant.SUCCESS_CODE)
                .list(flowInfoModelPage.toList())
                .totalSize(flowInfoModelPage.getTotalElements())
                .build();
    }
}
