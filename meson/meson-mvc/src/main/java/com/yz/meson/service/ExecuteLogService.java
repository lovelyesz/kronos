package com.yz.meson.service;

import com.yz.meson.dao.ExecuteLogRepository;
import com.yz.meson.enu.FlowState;
import com.yz.meson.enu.JobState;
import com.yz.meson.model.ExecuteLogModel;
import com.yz.meson.schedule.config.MesonJobExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shanchong
 * @date 2019-11-13
 **/
@Service
public class ExecuteLogService {

    @Autowired
    ExecuteLogRepository executeLogRepository;

    public ExecuteLogModel insert(MesonJobExecutor mesonJobExecutor){
        ExecuteLogModel executeLogModel = new ExecuteLogModel();
        executeLogModel.setCreateTime(new Date());
        executeLogModel.setFlowId(mesonJobExecutor.getFlowId());
        executeLogModel.setJobId(mesonJobExecutor.getJobId());
        executeLogModel.setState(JobState.INIT.code());
        executeLogModel.setRemark(JobState.INIT.desc());
        return executeLogRepository.save(executeLogModel);
    }

    public ExecuteLogModel update(Long id, JobState state){
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
}
