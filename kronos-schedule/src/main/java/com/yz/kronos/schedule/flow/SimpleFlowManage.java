package com.yz.kronos.schedule.flow;

import com.yz.kronos.ExecuteConstant;
import com.yz.kronos.JobInfo;
import com.yz.kronos.KubernetesConfig;
import com.yz.kronos.schedule.model.FlowInfo;
import com.yz.kronos.schedule.job.JobLaunchManage;
import com.yz.kronos.schedule.synchronizer.JobProcessSynchronizer;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 简单的工作流调度
 * @author shanchong
 * @date 2019-12-23
 **/
@Slf4j
public class SimpleFlowManage extends AbstractFlowManage {

    KubernetesConfig config;
    JobProcessSynchronizer jobProcessSynchronizer;
    FlowInterceptor flowInterceptor;
    JobLaunchManage jobLaunchManage;

    public void setConfig(KubernetesConfig config) {
        this.config = config;
    }

    public void setJobLaunchManage(JobLaunchManage jobLaunchManage) {
        this.jobLaunchManage = jobLaunchManage;
    }

    public void setJobProcessSynchronizer(JobProcessSynchronizer jobProcessSynchronizer) {
        this.jobProcessSynchronizer = jobProcessSynchronizer;
    }

    public void setFlowInterceptor(FlowInterceptor flowInterceptor) {
        this.flowInterceptor = flowInterceptor;
    }

    /**
     * 工作流执行调度
     *
     * @param flowInfo
     */
    @Override
    public void schedule(FlowInfo flowInfo) {
        final Long flowId = flowInfo.getFlowId();
        final List<FlowInfo.FlowElement> jobInfoList = flowInfo.getJobInfoList();
        final Map<Integer, List<FlowInfo.FlowElement>> sortJobListMap = jobInfoList.stream()
                .collect(Collectors.groupingBy(FlowInfo.FlowElement::getSort, Collectors.toList()));
        sortJobListMap.keySet().stream().sorted()
                .forEach(sort->{
                    if (flowInterceptor.intercept(flowId)){
                        log.warn("flow is intercepted flowId:{}",flowId);
                        return;
                    }
                    final List<FlowInfo.FlowElement> flowElements = sortJobListMap.getOrDefault(sort,new ArrayList<>());
                    log.info("flow is start schedule {},{}",flowElements,sort);
                    //同步器的key
                    String synchronizerKey = ExecuteConstant.KRONOS_EXECUTOR_SYNCHRONIZER + System.currentTimeMillis() + "_" + sort;
                    //sort相同的时候任务并行
                    flowElements.parallelStream()
                            .forEach(flowElement -> {
                                final JobInfo jobInfo = flowElement.getJobInfo();
                                jobInfo.setSynchronizerKey(synchronizerKey);
                                jobInfo.setJobId(flowElement.getJobId());
                                jobLaunchManage.schedule(flowId, jobInfo,config);
                            });
                    final long count = flowElements.parallelStream().mapToInt(f->f.getJobInfo().getShareTotal()).count();
                    jobProcessSynchronizer.init(synchronizerKey, (int) count);
                    jobProcessSynchronizer.wait(synchronizerKey, ExecuteConstant.KRONOS_EXECUTOR_EXPIRE_TIME,
                            ExecuteConstant.KRONOS_EXECUTOR_EXPIRE_TIME_UNIT);
                    log.info("job finish {}",flowElements);
                });
    }

    /**
     * 任务关停
     *
     * @param execIds
     */
    @Override
    public void shutdown(List<Long> execIds,Long flowId) {
        execIds.forEach(execId-> jobLaunchManage.shutdown(execId,config));
        log.warn("flow shutdown {},{}",execIds,flowId);
    }

}
