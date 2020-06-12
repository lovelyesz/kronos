package com.yz.kronos.config;

import com.yz.kronos.ExecuteConstant;
import com.yz.kronos.schedule.enu.JobStatus;
import com.yz.kronos.service.ExecuteLogService;
import com.yz.kronos.util.ExecuteUtil;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 执行日志记录监听处理
 * @author shanchong
 * @date 2019-12-23
 **/
@Slf4j
@Component
public class ExecuteLogEventHandler implements ResourceEventHandler<Job> {

    @Autowired
    private ExecuteLogService executeLogService;

    /**
     * Called when an object is added.
     *
     * @param obj object
     */
    @Override
    public void onAdd(Job obj) {

    }

    /**
     * Called when an object is modified. Note that oldObj is the last
     * known state of the object -- it is possible that several changes
     * were combined together, so you can't use this to see every single
     * change. It is also called when a re-list happens, and it will get
     * called even if nothing changes. This is useful for periodically
     * evaluating or syncing something.
     *
     * @param oldObj old object
     * @param newObj new object
     */
    @Override
    public void onUpdate(Job oldObj, Job newObj) {
        io.fabric8.kubernetes.api.model.batch.JobStatus status = newObj.getStatus();
        Map<String, String> labels = newObj.getMetadata().getLabels();
        final String execId = labels.get(ExecuteConstant.KRONOS_EXECUTE_ID);
        executeLogService.updateProcess(Long.valueOf(execId), status);
    }

    /**
     * Gets the final state of the item if it is known, otherwise
     * it would get an object of the DeletedFinalStateUnknown. This can
     * happen if the watch is closed and misses the delete event and
     * we don't notice the deletion until the subsequent re-list.
     *
     * @param obj
     * @param deletedFinalStateUnknown
     */
    @Override
    public void onDelete(Job obj, boolean deletedFinalStateUnknown) {
        Map<String, String> labels = obj.getMetadata().getLabels();
        final String execId = labels.get(ExecuteConstant.KRONOS_EXECUTE_ID);
        final Long execLogId = ExecuteUtil.getExecLogId(execId);
        executeLogService.updateStatus(execLogId, JobStatus.FAIL);
    }
}
