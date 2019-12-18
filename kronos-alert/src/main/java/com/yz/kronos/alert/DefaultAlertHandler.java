package com.yz.kronos.alert;

import com.yz.kronos.exception.JobException;
import com.yz.kronos.model.ExecuteJobInfo;

/**
 * 默认的告警处理类
 * @author shanchong
 * @date 2019-11-14
 **/
public class DefaultAlertHandler implements AlertHandler {

    @Override
    public void handle(ExecuteJobInfo executeJobInfo, JobException e) {
        //do nothing
    }

    @Override
    public void handle(JobException e) {
        handle(null,e);
    }
}
