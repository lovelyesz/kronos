package com.yz.meson.schedule;

import com.yz.meson.ExecuteConstant;
import com.yz.meson.schedule.config.KubernetesConfig;
import com.yz.meson.schedule.config.MesonJobExecutor;
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
    public void execute(Long flowId,List<MesonJobExecutor> jobExecutorList,
                        ScheduleListenerFactory scheduleListenerFactory){
        jobExecutorList.forEach(scheduleListenerFactory::init);
        Map<Integer, List<MesonJobExecutor>> sortJobExecutorMap = jobExecutorList.parallelStream()
                .collect(Collectors.groupingBy(MesonJobExecutor::getSort, Collectors.toList()));
        List<Integer> sortList = sortJobExecutorMap.keySet().parallelStream().sorted().collect(Collectors.toList());
        for (int i = 0; i < sortList.size(); i++) {
            Integer sort = sortList.get(i);
            //多个任务并行
            List<MesonJobExecutor> mesonJobExecutors = sortJobExecutorMap.get(sort);
            long count = mesonJobExecutors.parallelStream().collect(Collectors.summarizingInt(MesonJobExecutor::getShareCount)).getCount();
            //声明同步器
            String synchronizerKey = ExecuteConstant.MESON_EXECUTOR_SYNCHRONIZER + System.currentTimeMillis() + "_" + sort;
            scheduleSynchronizer.init(synchronizerKey, (int) count);

            //并行任务数量
            int size = mesonJobExecutors.size();
            log.info("({}/{}) meson job start {}", i+1,sortList.size(),mesonJobExecutors);
            for (int j = 0; j < size; j++) {
                MesonJobExecutor mesonJobExecutor = mesonJobExecutors.get(j);
                Map<String, String> labels = new HashMap<>();
                labels.put(ExecuteConstant.MESON_EXECUTE_SYNCHRONIZER_LABEL_NAME, synchronizerKey);
                mesonJobExecutor.setLabels(labels);

                JobScheduleFactory jobScheduleManager = new JobScheduleFactory(kubernetesConfig, mesonJobExecutor);
                jobScheduleManager.execute();
                scheduleListenerFactory.scheduled(mesonJobExecutor);
            }
            scheduleSynchronizer.wait(synchronizerKey, ExecuteConstant.MESON_EXECUTOR_EXPIRE_TIME, ExecuteConstant.MESON_EXECUTOR_EXPIRE_TIME_UNIT);
            log.info("({}/{}) meson job end {}", i+1,sortList.size(),mesonJobExecutors);

            boolean active = FlowInterrupter.getInstance().isActive(flowId);
            if (!active){
                log.error("meson flow {} is interrupter ",flowId);
                break;
            }
            for (MesonJobExecutor mesonJobExecutor : mesonJobExecutors){
                scheduleListenerFactory.finish(mesonJobExecutor);
            }
        }
        FlowInterrupter.getInstance().finish(flowId);
        log.info("meson flow is finish ");

    }

    public void shutdown(Long flowId,List<MesonJobExecutor> mesonJobExecutorList,
                         ScheduleListenerFactory scheduleListenerFactory) {
        FlowInterrupter.getInstance().interrupter(flowId);
        mesonJobExecutorList.forEach(mesonJobExecutor -> {
            scheduleListenerFactory.shutdown(mesonJobExecutor);
            JobScheduleFactory jobScheduleManager = new JobScheduleFactory(kubernetesConfig,mesonJobExecutor);
            List<Job> jobList = jobScheduleManager.shutdown();
            if (jobList.isEmpty()){
                return;
            }
            log.info("meson job is stopped {} - {} ",jobList.size(),jobList);
        });
    }
}
