package com.yz.kronos.service;

import com.yz.kronos.CallResultConstant;
import com.yz.kronos.dao.ExecuteLogRepository;
import com.yz.kronos.dao.FlowInfoRepository;
import com.yz.kronos.dao.JobInfoRepository;
import com.yz.kronos.schedule.enu.JobStatus;
import com.yz.kronos.schedule.model.ExecuteLogModel;
import com.yz.kronos.schedule.model.FlowInfoModel;
import com.yz.kronos.schedule.model.JobInfoModel;
import com.yz.kronos.schedule.model.PageResult;
import com.yz.kronos.schedule.repository.JobExecuteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    JobInfoRepository jobInfoRepository;
    @Autowired
    FlowInfoRepository flowInfoRepository;

    public ExecuteLogModel updateProcess(Long id, io.fabric8.kubernetes.api.model.batch.JobStatus status){
        final ExecuteLogModel executeLogModel = get(id);
        executeLogModel.setId(id);
        executeLogModel.setFinishTime(new Date());
        Integer succeed = Optional.ofNullable(status.getSucceeded()).orElse(0);
        Integer active = Optional.ofNullable(status.getActive()).orElse(0);
        Integer failed = Optional.ofNullable(status.getFailed()).orElse(0);
        executeLogModel.setFailedCount(failed);
        executeLogModel.setSucceedCount(succeed);
        executeLogModel.setActiveCount(active);
        executeLogModel.setStatus(JobStatus.RUNNING.code());
        executeLogModel.setRemark(JobStatus.RUNNING.desc());
        final Integer shareTotal = executeLogModel.getShareTotal();
        if (shareTotal.equals(succeed)){
            executeLogModel.setStatus(JobStatus.SUCCESS.code());
            executeLogModel.setRemark(JobStatus.SUCCESS.desc());
        }
        if (failed>0){
            executeLogModel.setStatus(JobStatus.FAIL.code());
            executeLogModel.setRemark(JobStatus.FAIL.desc());
        }
        return executeLogRepository.save(executeLogModel);
    }

    public int updateStatus(Long id, JobStatus state){
        return executeLogRepository.updateStatus(new Date(),state.code(),state.desc(),id);
    }

    public ExecuteLogModel get(Long execId){
        return executeLogRepository.findById(execId).get();
    }

    public List<ExecuteLogModel> findByFlowIdAndState(Long flowId, JobStatus... jobStatus){
        return executeLogRepository.findByFlowIdAndStatusIn(flowId, Arrays.stream(jobStatus).map(JobStatus::code).collect(Collectors.toList()));
    }

    public List<ExecuteLogModel> findByBatchNo(String batchNo){
        final List<ExecuteLogModel> executeLogModels = executeLogRepository.findByBatchNo(batchNo);
        final Set<Long> flowIds = executeLogModels.parallelStream().map(ExecuteLogModel::getFlowId).collect(Collectors.toSet());
        List<FlowInfoModel> flowInfoModelList = flowInfoRepository.findByIdIn(flowIds);
        final Map<Long, FlowInfoModel> flowInfoModelMap = flowInfoModelList.parallelStream().collect(Collectors.toMap(FlowInfoModel::getId, p -> p));
        final Set<Long> jobIds = executeLogModels.parallelStream().map(ExecuteLogModel::getJobId).collect(Collectors.toSet());
        List<JobInfoModel> jobInfoModelList = jobInfoRepository.findByIdIn(jobIds);
        final Map<Long, JobInfoModel> jobInfoModelMap = jobInfoModelList.parallelStream().collect(Collectors.toMap(JobInfoModel::getId, p -> p));

        executeLogModels.stream().peek(e->{
            e.setFlowInfo(flowInfoModelMap.getOrDefault(e.getFlowId(),new FlowInfoModel()));
            e.setJobInfo(jobInfoModelMap.getOrDefault(e.getJobId(),new JobInfoModel()));
        });
        return executeLogModels;
    }

    public PageResult<ExecuteLogModel> page(String flowName,Integer page,Integer limit){
        if (StringUtils.isEmpty(flowName)){
            return listAll(page,limit);
        }
        final List<FlowInfoModel> flowInfoModelList = flowInfoRepository.findByFlowNameLike("%"+flowName+"%");
        final Set<Long> flowIds = flowInfoModelList.parallelStream().map(FlowInfoModel::getId).collect(Collectors.toSet());
        final Page<ExecuteLogModel> executeLogModelPage = executeLogRepository.findByFlowIdIn(flowIds, PageRequest.of(page, limit, Sort.by(Sort.Order.desc("createTime"))));
        return getExecuteLogModels(executeLogModelPage);
    }

    public PageResult<ExecuteLogModel> listAll(Integer page,Integer limit){
        final Page<ExecuteLogModel> executeLogModelPage = executeLogRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Order.desc("createTime"))));
        return getExecuteLogModels(executeLogModelPage);
    }

    private PageResult<ExecuteLogModel> getExecuteLogModels(Page<ExecuteLogModel> executeLogModelList) {
        final Set<Long> jobIds = executeLogModelList.map(ExecuteLogModel::getJobId).stream().collect(Collectors.toSet());
        final Set<Long> flowIds = executeLogModelList.map(ExecuteLogModel::getFlowId).stream().collect(Collectors.toSet());
        List<FlowInfoModel> flowInfoModelList = flowInfoRepository.findByIdIn(flowIds);
        final Map<Long, FlowInfoModel> flowInfoModelMap = flowInfoModelList.parallelStream().collect(Collectors.toMap(FlowInfoModel::getId, p -> p));
        List<JobInfoModel> jobInfoModelList = jobInfoRepository.findByIdIn(jobIds);
        final Map<Long, JobInfoModel> jobInfoModelMap = jobInfoModelList.parallelStream().collect(Collectors.toMap(JobInfoModel::getId, p -> p));
        executeLogModelList.forEach(e->{
            e.setJobInfo(jobInfoModelMap.getOrDefault(e.getJobId(),new JobInfoModel()));
            e.setFlowInfo(flowInfoModelMap.getOrDefault(e.getFlowId(),new FlowInfoModel()));
        });
        final PageResult<ExecuteLogModel> pageResult = new PageResult<>();
        pageResult.setCode(CallResultConstant.SUCCESS_CODE);
        pageResult.setList(executeLogModelList.toList());
        pageResult.setTotalSize(executeLogModelList.getTotalElements());
        return pageResult;
    }


    @Override
    public Long insert(Long flowId, Long jobId,Integer shareTotal,String batchNo) {
        ExecuteLogModel executeLogModel = new ExecuteLogModel();
        executeLogModel.setCreateTime(new Date());
        executeLogModel.setFlowId(flowId);
        executeLogModel.setJobId(jobId);
        executeLogModel.setStatus(JobStatus.INIT.code());
        executeLogModel.setRemark(JobStatus.INIT.desc());
        executeLogModel.setShareTotal(shareTotal);
        executeLogModel.setActiveCount(0);
        executeLogModel.setSucceedCount(0);
        executeLogModel.setFailedCount(0);
        executeLogModel.setBatchNo(batchNo);
        executeLogRepository.save(executeLogModel);
        return executeLogModel.getId();
    }
}
