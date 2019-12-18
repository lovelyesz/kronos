package com.yz.kronos.config;

import com.yz.kronos.ExecuteConstant;
import com.yz.kronos.alert.AlertHandler;
import com.yz.kronos.enu.JobState;
import com.yz.kronos.model.ExecuteJobInfo;
import com.yz.kronos.schedule.JobQueueManage;
import com.yz.kronos.model.ExecuteLogModel;
import com.yz.kronos.schedule.config.JobExecutor;
import com.yz.kronos.schedule.ScheduleListenerFactory;
import com.yz.kronos.service.ExecuteLogService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 工作流生命周期监听器 init->scheduled->finish  任务初始化->调用k8s->任务执行完成
 * @author shanchong
 * @date 2019-11-12
 **/
@Service
public class FlowScheduleListenerFactory implements ScheduleListenerFactory {

    @Autowired
    RedissonClient redissonClient;
    @Autowired(required = false)
    AlertHandler alertHandler;
    @Autowired
    JobQueueManage redisJobQueueManager;
    @Autowired
    ExecuteLogService executeLogService;

    @Override
    public void init(JobExecutor jobExecutor) {
        //执行记录日志
        ExecuteLogModel executeLogModel = executeLogService.insert(jobExecutor);
        Long execLogId = executeLogModel.getId();
        jobExecutor.setExecLogId(execLogId);
        saveQueue(jobExecutor);
    }

    /**
     * 将任务执行信息存入redis中，方便executor获取
     * @param jobExecutor
     * @return
     */
    private void saveQueue(JobExecutor jobExecutor){
        for (int i = 0; i < jobExecutor.getShareCount(); i++) {
            ExecuteJobInfo executeJobInfo = new ExecuteJobInfo();
            executeJobInfo.setExecId(jobExecutor.getExecId());
            executeJobInfo.setClazz(jobExecutor.getClazz());
            executeJobInfo.setTotalPartitionNums(jobExecutor.getShareCount());
            executeJobInfo.setPartitions(i);
            redisJobQueueManager.add(ExecuteConstant.KRONOS_EXECUTOR_QUEUE_NAME_PRE + jobExecutor.getExecId(), executeJobInfo);
        }
    }

    @Override
    public void scheduled(JobExecutor jobExecutor) {
        executeLogService.update(jobExecutor.getExecLogId(), JobState.SCHEDULED);
    }

    @Override
    public void finish(JobExecutor jobExecutor) {
        executeLogService.update(jobExecutor.getExecLogId(),JobState.SUCCESS);
    }

    /**
     * 强行终止某个任务
     *
     * @param jobExecutor
     */
    @Override
    public void shutdown(JobExecutor jobExecutor) {
        redisJobQueueManager.remove(ExecuteConstant.KRONOS_EXECUTOR_QUEUE_NAME_PRE + jobExecutor.getExecId());
    }


}
