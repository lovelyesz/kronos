package com.yz.kronos.schedule.listener;

import com.yz.kronos.ExecuteConstant;
import com.yz.kronos.schedule.synchronizer.JobProcessSynchronizer;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.api.model.batch.JobStatus;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

/**
 * 任务进程同步器处理
 * @author shanchong
 * @date 2019-12-20
 **/
@Slf4j
public class JobSynchronzeEventHandler implements ResourceEventHandler<Job> {

    JobProcessSynchronizer jobProcessSynchronizer;

    public JobSynchronzeEventHandler(JobProcessSynchronizer jobProcessSynchronizer) {
        this.jobProcessSynchronizer = jobProcessSynchronizer;
    }

    @Override
    public void onAdd(Job obj) {
        log.info("launch listener to {}",obj.getMetadata().getName());
    }

    @Override
    public void onUpdate(Job oldObj, Job newObj) {
        JobStatus status = newObj.getStatus();
        Integer succeed = status.getSucceeded();
        Integer active = status.getActive();
        Integer failed = status.getFailed();
        Integer completions = newObj.getSpec().getCompletions();
        if (succeed==null){
            return;
        }
        log.info("callback job {} process is ({}/{}/{}/{})",newObj.getMetadata().getName(),
                active,succeed,failed,completions);
        if (succeed.equals(completions)){
            Map<String, String> labels = newObj.getMetadata().getLabels();
            log.info("kubernetes callback success {}",newObj);
            String key = labels.get(ExecuteConstant.KRONOS_EXECUTE_SYNCHRONIZER_LABEL_NAME);
            if (key!=null){
                jobProcessSynchronizer.countDown(key);
            }
        }
    }

    @Override
    public void onDelete(Job obj, boolean deletedFinalStateUnknown) {
        //存活的pod数量
        Integer active = obj.getStatus().getActive();
        Integer succeeded = obj.getStatus().getSucceeded();
        Integer failed = obj.getStatus().getFailed();
        log.info("callback pod {} is closed ({}/{}/{})",obj.getMetadata().getName(),active,succeeded,failed);
        Map<String, String> labels = obj.getMetadata().getLabels();
        String key = labels.get(ExecuteConstant.KRONOS_EXECUTE_SYNCHRONIZER_LABEL_NAME);
        if (key!=null){
            jobProcessSynchronizer.delete(key);
        }
    }
}
