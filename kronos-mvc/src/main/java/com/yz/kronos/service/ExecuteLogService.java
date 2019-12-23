package com.yz.kronos.service;

import com.yz.kronos.dao.ExecuteLogRepository;
import com.yz.kronos.enu.JobState;
import com.yz.kronos.model.ExecuteLogModel;
import com.yz.kronos.schedule.repository.JobExecuteRepository;
import io.fabric8.kubernetes.api.model.batch.JobStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author shanchong
 * @date 2019-11-13
 **/
@Service
public class ExecuteLogService implements JobExecuteRepository {

    @Autowired
    ExecuteLogRepository executeLogRepository;

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
        executeLogModel.setState(JobState.SCHEDULED.code());
        executeLogModel.setRemark(JobState.SCHEDULED.desc());
        return executeLogRepository.save(executeLogModel);
    }

    public ExecuteLogModel update(Long id,JobState state){
        ExecuteLogModel executeLogModel = new ExecuteLogModel();
        executeLogModel.setId(id);
        executeLogModel.setState(state.code());
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

    public List<ExecuteLogModel> list(ExecuteLogModel model){
        return executeLogRepository.findAll(Example.of(model));
    }

    @Override
    public Long insert(Long flowId, Long jobId,Integer shareTotal) {
        ExecuteLogModel executeLogModel = new ExecuteLogModel();
        executeLogModel.setCreateTime(new Date());
        executeLogModel.setFlowId(flowId);
        executeLogModel.setJobId(jobId);
        executeLogModel.setState(JobState.INIT.code());
        executeLogModel.setRemark(JobState.INIT.desc());
        executeLogModel.setShareTotal(shareTotal);
        executeLogModel.setActiveCount(0);
        executeLogModel.setSucceedCount(0);
        executeLogModel.setFailedCount(0);
        executeLogRepository.save(executeLogModel);
        return executeLogModel.getId();
    }
}
