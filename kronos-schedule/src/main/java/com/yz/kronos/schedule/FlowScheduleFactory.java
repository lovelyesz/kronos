package com.yz.kronos.schedule;

import com.yz.kronos.ExecuteConstant;
import com.yz.kronos.schedule.config.KubernetesConfig;
import com.yz.kronos.schedule.config.JobExecutor;
import io.fabric8.kubernetes.api.model.batch.Job;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工作流管理器
 * @author shanchong
 * @date 2019-11-12
 **/
@Slf4j
public class FlowScheduleFactory {

    KubernetesConfig kubernetesConfig;
    ScheduleSynchronizer scheduleSynchronizer;

    public FlowScheduleFactory(ScheduleSynchronizer scheduleSynchronizer,
                               KubernetesConfig kubernetesConfig){
        this.kubernetesConfig = kubernetesConfig;
        this.scheduleSynchronizer = scheduleSynchronizer;
    }

    /**
     * 执行
     * @param jobExecutorList 把它看成一个链表
     * @param scheduleListenerFactory 任务仓库
     */
    public void execute(Long flowId,List<JobExecutor> jobExecutorList,
                        ScheduleListenerFactory scheduleListenerFactory){
        jobExecutorList.forEach(scheduleListenerFactory::init);
        Map<Integer, List<JobExecutor>> sortJobExecutorMap = jobExecutorList.parallelStream()
                .collect(Collectors.groupingBy(JobExecutor::getSort, Collectors.toList()));
        List<Integer> sortList = sortJobExecutorMap.keySet().parallelStream().sorted().collect(Collectors.toList());
        for (int i = 0; i < sortList.size(); i++) {
            Integer sort = sortList.get(i);
            //多个任务并行
            List<JobExecutor> jobExecutors = sortJobExecutorMap.get(sort);
            long count = jobExecutors.parallelStream().collect(Collectors.summarizingInt(JobExecutor::getShareCount)).getCount();
            //声明同步器
            String synchronizerKey = ExecuteConstant.KRONOS_EXECUTOR_SYNCHRONIZER + System.currentTimeMillis() + "_" + sort;
            scheduleSynchronizer.init(synchronizerKey, (int) count);

            //并行任务数量
            int size = jobExecutors.size();
            log.info("({}/{}) kronos job start {}", i+1,sortList.size(), jobExecutors);
            for (int j = 0; j < size; j++) {
                JobExecutor jobExecutor = jobExecutors.get(j);
                Map<String, String> labels = new HashMap<>();
                labels.put(ExecuteConstant.KRONOS_EXECUTE_SYNCHRONIZER_LABEL_NAME, synchronizerKey);
                jobExecutor.setLabels(labels);

                JobScheduleFactory jobScheduleManager = new JobScheduleFactory(kubernetesConfig, jobExecutor);
                jobScheduleManager.execute();
                scheduleListenerFactory.scheduled(jobExecutor);
            }
            scheduleSynchronizer.wait(synchronizerKey, ExecuteConstant.KRONOS_EXECUTOR_EXPIRE_TIME, ExecuteConstant.KRONOS_EXECUTOR_EXPIRE_TIME_UNIT);
            log.info("({}/{}) kronos job end {}", i+1,sortList.size(), jobExecutors);

            boolean active = FlowInterrupter.getInstance().isActive(flowId);
            if (!active){
                log.error("kronos flow {} is interrupter ",flowId);
                break;
            }
            for (JobExecutor jobExecutor : jobExecutors){
                scheduleListenerFactory.finish(jobExecutor);
            }
        }
        FlowInterrupter.getInstance().finish(flowId);
        log.info("kronos flow is finish ");

    }

    public void shutdown(Long flowId,List<JobExecutor> jobExecutorList,
                         ScheduleListenerFactory scheduleListenerFactory) {
        FlowInterrupter.getInstance().interrupter(flowId);
        jobExecutorList.forEach(jobExecutor -> {
            scheduleListenerFactory.shutdown(jobExecutor);
            JobScheduleFactory jobScheduleManager = new JobScheduleFactory(kubernetesConfig,jobExecutor);
            List<Job> jobList = jobScheduleManager.shutdown();
            if (jobList.isEmpty()){
                return;
            }
            log.info("kronos job is stopped {} - {} ",jobList.size(),jobList);
        });
    }
}
