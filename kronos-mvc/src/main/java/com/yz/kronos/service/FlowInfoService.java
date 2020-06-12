package com.yz.kronos.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.yz.kronos.CallResultConstant;
import com.yz.kronos.JobInfo;
import com.yz.kronos.KubernetesConfig;
import com.yz.kronos.dao.*;
import com.yz.kronos.schedule.enu.FlowStatus;
import com.yz.kronos.schedule.enu.JobStatus;
import com.yz.kronos.enums.YesNoEnum;
import com.yz.kronos.schedule.enu.ImagePillPolicy;
import com.yz.kronos.schedule.flow.AbstractFlowManage;
import com.yz.kronos.schedule.model.*;
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
public class FlowInfoService {

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
    private ExecuteLogRepository executeLogRepository;

    public FlowInfoModel get(Long id){
        return flowInfoRepository.findById(id).get();
    }

    @Async
    public void schedule(Long flowId) {
        FlowInfoModel flowInfoModel = flowInfoRepository.findById(flowId).get();
        if (!FlowStatus.RUNNABLE.code().equals(flowInfoModel.getStatus())) {
            log.error("kronos flow execute fail , flow {} status is {}", flowId, flowInfoModel.getStatus());
            return;
        }
        final String batchNo = UUID.randomUUID().toString().replaceAll("-","");

        flowInfoModel.setStatus(FlowStatus.RUNNING.code());
        flowInfoModel.setLastBatchNo(batchNo);
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
            jobInfo.setShareTotal(e.getShareTotal());
            jobInfo.setResources(JSONObject.parseObject(jobInfoModel.getResources()));
            final JobInfo.NamespaceInfo namespaceInfo = new JobInfo.NamespaceInfo(namespaceInfoModel);
            jobInfo.setNamespace(namespaceInfo);
            jobInfo.setBatchNo(batchNo);
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

        flowInfoModel.setStatus(FlowStatus.RUNNABLE.code());
        flowInfoRepository.save(flowInfoModel);

    }

    @Async
    public void shutdown(Long flowId) {
        FlowInfoModel flowInfoModel = flowInfoRepository.findById(flowId).get();
        log.warn("kronos flow is stopping flow:{}",flowId);
        final Integer state = flowInfoModel.getStatus();
        if (!FlowStatus.RUNNING.code().equals(state)){
            log.error("kronos flow stop fail , flow {} status is {}", flowId, flowInfoModel.getStatus());
            return;
        }
        flowInfoModel.setStatus(FlowStatus.RUNNABLE.code());
        //更新工作流状态
        flowInfoRepository.save(flowInfoModel);

        List<ExecuteLogModel> executeLogModelList =
                executeLogRepository.findByFlowIdAndStatusIn(flowId,
                        Lists.newArrayList(JobStatus.RUNNING, JobStatus.INIT).stream()
                .map(JobStatus::code).collect(Collectors.toList()));

        if (executeLogModelList.isEmpty()){
            log.error("kronos flow not find status is 0 or 1 of execute log flowId:{}",flowId);
            return;
        }
        final List<Long> execIds = executeLogModelList.parallelStream().map(e->e.getId())
                .collect(Collectors.toList());
        flowManage.shutdown(execIds,flowId);

        executeLogModelList.forEach(executeLogModel->{
            executeLogRepository.updateStatus(new Date(), JobStatus.SHUTDOWN.code(), JobStatus.SHUTDOWN.desc(),
                    executeLogModel.getId());
        });


    }

    public List<FlowInfoModel> selectByIds(Set<Long> flowIds) {
        return flowInfoRepository.findByIdIn(flowIds);
    }

    public List<FlowInfoModel> selectByFlowName(String flowName) {
        return flowInfoRepository.findByFlowNameLike(flowName);
    }

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
        final PageResult<FlowInfoModel> pageResult = new PageResult<>();
        pageResult.setCode(CallResultConstant.SUCCESS_CODE);
        pageResult.setList(flowInfoModelPage.toList());
        pageResult.setTotalSize(flowInfoModelPage.getTotalElements());
        return pageResult;
    }

    public void update(FlowInfoModel model) {
        final FlowInfoModel flowInfoModel = flowInfoRepository.findById(model.getId()).get();
        final Integer status = model.getStatus();
        final String cron = model.getCron();
        if (status!=null){
            flowInfoModel.setStatus(status);
        }
        if (cron!=null){
            flowInfoModel.setCron(cron);
        }
        flowInfoRepository.save(flowInfoModel);
    }
}
