package com.yz.kronos.alert;

import com.yz.kronos.exception.JobException;
import com.yz.kronos.model.ExecuteJobInfo;

/**
 * 告警处理器
 * @author shanchong
 */
public interface AlertHandler {

    void handle(ExecuteJobInfo executeJobInfo, JobException e);

    void handle(JobException e);

}
