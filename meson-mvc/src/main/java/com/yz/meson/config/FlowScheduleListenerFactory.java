package com.yz.meson.config;

import com.yz.meson.ExecuteConstant;
import com.yz.meson.alert.MesonAlertHandler;
import com.yz.meson.enu.JobState;
import com.yz.meson.schedule.MesonJobQueueManage;
import com.yz.meson.model.ExecuteLogModel;
import com.yz.meson.model.ExecuteJobInfo;
import com.yz.meson.schedule.config.MesonJobExecutor;
import com.yz.meson.schedule.ScheduleListenerFactory;
import com.yz.meson.service.ExecuteLogService;
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
    MesonAlertHandler mesonAlertHandler;
    @Autowired
    MesonJobQueueManage redisJobQueueManager;
    @Autowired
    ExecuteLogService executeLogService;

    @Override
    public void init(MesonJobExecutor mesonJobExecutor) {
        //执行记录日志
        ExecuteLogModel executeLogModel = executeLogService.insert(mesonJobExecutor);
        Long execLogId = executeLogModel.getId();
        mesonJobExecutor.setExecLogId(execLogId);
        saveQueue(mesonJobExecutor);
    }

    /**
     * 将任务执行信息存入redis中，方便executor获取
     * @param mesonJobExecutor
     * @return
     */
    private void saveQueue(MesonJobExecutor mesonJobExecutor){
        for (int i = 0; i < mesonJobExecutor.getShareCount(); i++) {
            ExecuteJobInfo executeJobInfo = new ExecuteJobInfo();
            executeJobInfo.setExecId(mesonJobExecutor.getExecId());
            executeJobInfo.setClazz(mesonJobExecutor.getClazz());
            executeJobInfo.setTotalPartitionNums(mesonJobExecutor.getShareCount());
            executeJobInfo.setPartitions(i);
            redisJobQueueManager.add(ExecuteConstant.MESON_EXECUTOR_QUEUE_NAME_PRE + mesonJobExecutor.getExecId(), executeJobInfo);
        }
    }

    @Override
    public void scheduled(MesonJobExecutor jobExecutor) {
        executeLogService.update(jobExecutor.getExecLogId(),JobState.SCHEDULED);
    }

    @Override
    public void finish(MesonJobExecutor jobExecutor) {
        executeLogService.update(jobExecutor.getExecLogId(),JobState.SUCCESS);
    }

    /**
     * 强行终止某个任务
     *
     * @param mesonJobExecutor
     */
    @Override
    public void shutdown(MesonJobExecutor mesonJobExecutor) {
        redisJobQueueManager.remove(ExecuteConstant.MESON_EXECUTOR_QUEUE_NAME_PRE+mesonJobExecutor.getExecId());
    }


}
