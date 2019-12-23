package com.yz.kronos.schedule.job;

import com.alibaba.fastjson.JSONObject;
import com.yz.kronos.model.KubernetesConfig;
import com.yz.kronos.schedule.handle.StartJobHandle;
import com.yz.kronos.schedule.listener.JobScheduleCycle;
import com.yz.kronos.schedule.queue.JobQueue;
import com.yz.kronos.schedule.repository.JobExecuteRepository;
import com.yz.kronos.util.ExecuteUtil;

/**
 * kubernetes Job调度，任务被调度后k8s将异步进行执行，调度执行完成并不是任务执行完成
 * @author shanchong
 */
public interface JobSchedule {

    /**
     * k8s配置
     * @return
     */
    KubernetesConfig config();

    /**
     * 调度触发
     * @param flowId
     * @param jobInfo
     * @return execId
     */
    Long schedule(Long flowId,JobInfo jobInfo);

    /**
     * 任务信息队列
     * @return
     */
    JobQueue queue();

    /**
     * 任务监听器
     * @return
     */
    JobScheduleCycle cycle();

    /**
     * 任务执行记录库
     * @return
     */
    JobExecuteRepository repository();

    abstract class BaseJobSchedule implements JobSchedule {

        @Override
        public Long schedule(Long flowId,JobInfo jobInfo) {
            final KubernetesConfig config = config();
            final JobQueue queue = queue();
            final Integer shareTotal = jobInfo.getShareTotal();
            //记录执行日志
            final Long execId = repository().insert(flowId, jobInfo.getJobId());
            final String execId1 = ExecuteUtil.getExecId(execId, flowId, jobInfo.getJobId());
            queue.add(config.getExecutorQueueNamePre()+execId1,
                    JSONObject.toJSONString(jobInfo),shareTotal);
            final JobScheduleCycle cycle = cycle();
            cycle.startJob();
            final StartJobHandle startJobHandle = new StartJobHandle(config, jobInfo);
            startJobHandle.startJob(execId1);
            cycle.processJob();
            return execId;
        }
    }

}
