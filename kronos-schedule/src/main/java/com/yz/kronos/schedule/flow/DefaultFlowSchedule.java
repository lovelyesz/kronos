package com.yz.kronos.schedule.flow;

import com.yz.kronos.model.KubernetesConfig;
import com.yz.kronos.schedule.job.JobInfo;
import com.yz.kronos.schedule.job.JobSchedule;
import com.yz.kronos.schedule.repository.JobExecuteRepository;
import com.yz.kronos.schedule.synchronizer.JobProcessSynchronizer;
import com.yz.kronos.util.ExecuteUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 简单的工作流调度
 * @author shanchong
 * @date 2019-12-23
 **/
@Slf4j
public class DefaultFlowSchedule implements FlowSchedule {

    KubernetesConfig config;
    JobSchedule jobSchedule;
    JobProcessSynchronizer jobProcessSynchronizer;

    public DefaultFlowSchedule(KubernetesConfig config, JobSchedule jobSchedule,
                               JobProcessSynchronizer jobProcessSynchronizer) {
        this.config = config;
        this.jobSchedule = jobSchedule;
        this.jobProcessSynchronizer = jobProcessSynchronizer;
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
                    String synchronizerKey = executorSynchronizerNamePre + System.currentTimeMillis() + "_" + sort;
                    //sort相同的时候任务并行
                    flowElements.parallelStream()
                            .forEach(flowElement -> {
                                final JobInfo jobInfo = flowElement.getJobInfo();
                                jobInfo.setSynchronizerKey(synchronizerKey);
                                jobInfo.setJobId(flowElement.getJobId());
                                jobSchedule.schedule(flowInfo.getFlowId(), jobInfo);
                            });
                    final long count = flowElements.parallelStream().mapToInt(FlowInfo.FlowElement::getSort).count();
                    jobProcessSynchronizer.init(synchronizerKey, (int) count);
                    jobProcessSynchronizer.wait(synchronizerKey,1, TimeUnit.HOURS);
                });

    }

    @Override
    public KubernetesConfig config() {
        return config;
    }

}
