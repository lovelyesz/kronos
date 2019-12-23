package com.yz.kronos.service;

import com.yz.kronos.dao.FlowInfoRepository;
import com.yz.kronos.dao.JobInfoRepository;
import com.yz.kronos.dao.JobRelationRepository;
import com.yz.kronos.enums.YesNoEnum;
import com.yz.kronos.model.FlowInfoModel;
import com.yz.kronos.model.JobInfoModel;
import com.yz.kronos.model.JobRelationModel;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shanchong
 * @date 2019-11-07
 **/
@Service
public class FlowInfoService {

    @Autowired
    FlowInfoRepository flowInfoRepository;
    @Autowired
    JobRelationRepository jobRelationRepository;
    @Autowired
    JobInfoRepository jobInfoRepository;

    public FlowInfoModel get(Long id){
        return flowInfoRepository.findById(id).get();
    }

    public FlowInfoModel save(FlowInfoModel flowInfoModel){
        return flowInfoRepository.save(flowInfoModel);
    }

    public void delete(Long id){
        final FlowInfoModel flowInfoModel = flowInfoRepository.findById(id).get();
        flowInfoModel.setIsDelete(YesNoEnum.YES.code());
        flowInfoRepository.save(flowInfoModel);
    }

    public List<FlowInfoModel> list(FlowInfoModel flowInfoModel){
        flowInfoModel.setIsDelete(YesNoEnum.NO.code());
        return flowInfoRepository.findAll(Example.of(flowInfoModel));
    }

    public List<FlowInfoModel> list(Long namespaceId){
        final List<FlowInfoModel> flowInfoModels = flowInfoRepository.findByNamespaceIdAndIsDelete(namespaceId, YesNoEnum.NO.code());
        final Set<Long> flowIds = flowInfoModels.parallelStream().map(FlowInfoModel::getId).collect(Collectors.toSet());
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

        return flowInfoModels.parallelStream().peek(flowInfoModel->{
            final Long flowId = flowInfoModel.getId();
            final List<JobInfoModel> jobList = flowIdJobMap.getOrDefault(flowId, Lists.emptyList());
            flowInfoModel.setJobList(jobList);
        }).collect(Collectors.toList());
    }
}
