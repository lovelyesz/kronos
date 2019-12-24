package com.yz.kronos.schedule.flow;

import com.yz.kronos.ExecuteConstant;
import com.yz.kronos.JobInfo;
import com.yz.kronos.KubernetesConfig;
import com.yz.kronos.schedule.job.JobSchedule;
import com.yz.kronos.schedule.job.JobShutdown;
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
public class DefaultFlowManage extends AbstractFlowManage {

    KubernetesConfig config;
    JobSchedule jobSchedule;
    JobProcessSynchronizer jobProcessSynchronizer;
    JobShutdown jobShutdown;

    public DefaultFlowManage(KubernetesConfig config, JobSchedule jobSchedule,
                             JobProcessSynchronizer jobProcessSynchronizer,
                             JobShutdown jobShutdown) {
        this.config = config;
        this.jobSchedule = jobSchedule;
        this.jobProcessSynchronizer = jobProcessSynchronizer;
        this.jobShutdown = jobShutdown;
    }

    /**
     * 工作流执行调度
     *
     * @param flowInfo
     */
    @Override
    public void schedule(FlowInfo flowInfo) {
        final List<FlowInfo.FlowElement> jobInfoList = flowInfo.getJobInfoList();
        final Map<Integer, List<FlowInfo.FlowElement>> sortJobListMap = jobInfoList.stream()
                .collect(Collectors.groupingBy(FlowInfo.FlowElement::getSort, Collectors.toList()));
        sortJobListMap.keySet().stream().sorted()
                .forEach(sort->{
                    final List<FlowInfo.FlowElement> flowElements = sortJobListMap.getOrDefault(sort,new ArrayList<>());
                    final String executorSynchronizerNamePre = config.getExecutorSynchronizerNamePre();
                    //同步器的key
                    String synchronizerKey = executorSynchronizerNamePre + System.currentTimeMillis() + "_" + sort;
                    //sort相同的时候任务并行
                    flowElements.parallelStream()
                            .forEach(flowElement -> {
                                final JobInfo jobInfo = flowElement.getJobInfo();
                                jobInfo.setSynchronizerKey(synchronizerKey);
                                jobInfo.setJobId(flowElement.getJobId());
                                jobSchedule.schedule(flowInfo.getFlowId(), jobInfo,config);
                            });
                    final long count = flowElements.parallelStream().mapToInt(FlowInfo.FlowElement::getSort).count();
                    jobProcessSynchronizer.init(synchronizerKey, (int) count);
                    jobProcessSynchronizer.wait(synchronizerKey, ExecuteConstant.KRONOS_EXECUTOR_EXPIRE_TIME,
                            ExecuteConstant.KRONOS_EXECUTOR_EXPIRE_TIME_UNIT);
                });

    }

    /**
     * 任务关停
     *
     * @param execIds
     */
    @Override
    public void shutdown(List<String> execIds) {
        execIds.forEach(execId->jobShutdown.shutdown(execId,config));
    }

}
