package com.yz.kronos.service;

import com.yz.kronos.dao.ExecuteLogRepository;
import com.yz.kronos.enu.JobState;
import com.yz.kronos.model.ExecuteLogModel;
import com.yz.kronos.model.FlowInfoModel;
import com.yz.kronos.model.JobInfoModel;
import com.yz.kronos.schedule.repository.JobExecuteRepository;
import io.fabric8.kubernetes.api.model.batch.JobStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shanchong
 * @date 2019-11-13
 **/
@Service
public class ExecuteLogService implements JobExecuteRepository {

    @Autowired
    ExecuteLogRepository executeLogRepository;
    @Autowired
    JobInfoService jobInfoService;
    @Autowired
    FlowInfoService flowInfoService;

    public ExecuteLogModel update(Long id, JobStatus status){
        ExecuteLogModel executeLogModel = new ExecuteLogModel();
        executeLogModel.setId(id);
        executeLogModel.setFinishTime(new Date());
        Integer succeed = Optional.ofNullable(status.getSucceeded()).orElse(0);
        Integer active = Optional.ofNullable(status.getActive()).orElse(0);
        Integer failed = Optional.ofNullable(status.getFailed()).orElse(0);
        executeLogModel.setFailedCount(failed);
        executeLogModel.setSucceedCount(succeed);
        executeLogModel.setActiveCount(active);
        executeLogModel.setStatus(JobState.RUNNING.code());
        executeLogModel.setRemark(JobState.RUNNING.desc());
        return executeLogRepository.save(executeLogModel);
    }

    public ExecuteLogModel update(Long id,JobState state){
        ExecuteLogModel executeLogModel = new ExecuteLogModel();
        executeLogModel.setId(id);
        executeLogModel.setStatus(state.code());
        executeLogModel.setRemark(state.desc());
        executeLogModel.setFinishTime(new Date());
        return executeLogRepository.save(executeLogModel);
    }

    public ExecuteLogModel get(Long execId){
        return executeLogRepository.findById(execId).get();
    }

    public List<ExecuteLogModel> findByFlowIdAndState(Long flowId, JobState... jobState){
        return executeLogRepository.findByFlowIdAndStateIn(flowId, Arrays.stream(jobState).map(JobState::code).collect(Collectors.toList()));
    }

    public Page<ExecuteLogModel> page(ExecuteLogModel model,Integer pageNum,Integer pageSize){
        final Example<ExecuteLogModel> example = Example.of(model);
        final Sort sort = Sort.by(Sort.Order.desc("createTime"));
        final PageRequest pageRequest = PageRequest.of(pageNum, pageSize, sort);
        final Page<ExecuteLogModel> page = executeLogRepository.findAll(example, pageRequest);
        final Set<Long> jobIds = page.map(ExecuteLogModel::getJobId).stream().collect(Collectors.toSet());
        final Set<Long> flowIds = page.map(ExecuteLogModel::getFlowId).stream().collect(Collectors.toSet());
        List<FlowInfoModel> flowInfoModelList = flowInfoService.selectByIds(flowIds);
        final Map<Long, FlowInfoModel> flowInfoModelMap = flowInfoModelList.parallelStream().collect(Collectors.toMap(FlowInfoModel::getId, p -> p));
        List<JobInfoModel> jobInfoModelList = jobInfoService.selectByIds(jobIds);
        final Map<Long, JobInfoModel> jobInfoModelMap = jobInfoModelList.parallelStream().collect(Collectors.toMap(JobInfoModel::getId, p -> p));
        page.forEach(e->{
            e.setJobInfo(jobInfoModelMap.getOrDefault(e.getJobId(),new JobInfoModel()));
            e.setFlowInfo(flowInfoModelMap.getOrDefault(e.getFlowId(),new FlowInfoModel()));
        });
        return page;
    }

    @Override
    public Long insert(Long flowId, Long jobId,Integer shareTotal) {
        ExecuteLogModel executeLogModel = new ExecuteLogModel();
        executeLogModel.setCreateTime(new Date());
        executeLogModel.setFlowId(flowId);
        executeLogModel.setJobId(jobId);
        executeLogModel.setStatus(JobState.INIT.code());
        executeLogModel.setRemark(JobState.INIT.desc());
        executeLogModel.setShareTotal(shareTotal);
        executeLogModel.setActiveCount(0);
        executeLogModel.setSucceedCount(0);
        executeLogModel.setFailedCount(0);
        executeLogRepository.save(executeLogModel);
        return executeLogModel.getId();
    }
}
