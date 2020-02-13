package com.yz.kronos.schedule.listener;

import com.yz.kronos.ExecuteConstant;
import com.yz.kronos.message.alert.AlertHandler;
import com.yz.kronos.message.alert.DefaultAlertHandler;
import com.yz.kronos.schedule.execption.ScheduleException;
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
public class JobSynchronousEventHandler implements ResourceEventHandler<Job> {

    /**
     * 任务进度同步器
     */
    JobProcessSynchronizer jobProcessSynchronizer;
    /**
     * 告警处理
     */
    AlertHandler alertHandler = new DefaultAlertHandler();

    public void setJobProcessSynchronizer(JobProcessSynchronizer jobProcessSynchronizer) {
        this.jobProcessSynchronizer = jobProcessSynchronizer;
    }

    public void setAlertHandler(final AlertHandler alertHandler) {
        this.alertHandler = alertHandler;
    }

    @Override
    public void onAdd(Job obj) {
        log.info("launch listener to namespace:{} name:{}",obj.getMetadata().getNamespace(),obj.getMetadata().getName());
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

        final String namespace = newObj.getMetadata().getNamespace();
        log.info("callback namespace:{} job {} process is ({}/{}/{}/{})", namespace,newObj.getMetadata().getName(),
                active,succeed,failed,completions);
        //失败后告警
        if (failed>0){
            alertHandler.handle(new ScheduleException(newObj.getMetadata().getName()+" execute fail labels="
                    +newObj.getMetadata().getLabels()));
        }
        //执行成功的数量 = 分片的总数
        if (succeed.equals(completions)){
            Map<String, String> labels = newObj.getMetadata().getLabels();
            log.info("kubernetes namespace:{} callback success {}",namespace,newObj);
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
        final String namespace = obj.getMetadata().getNamespace();
        log.info("callback namespace:{} pod {} is closed ({}/{}/{})",namespace,obj.getMetadata().getName(),active,succeeded,failed);
        Map<String, String> labels = obj.getMetadata().getLabels();
        String key = labels.get(ExecuteConstant.KRONOS_EXECUTE_SYNCHRONIZER_LABEL_NAME);
        if (key!=null){
            jobProcessSynchronizer.delete(key);
        }
    }
}
