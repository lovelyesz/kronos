package com.yz.kronos.quartz;

import com.yz.kronos.service.FlowInfoService;
import com.yz.kronos.util.SpringHelperUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author shanchong
 * @date 2019-11-13
 **/
@Slf4j
public class FlowScheduleJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        FlowInfoService scheduleService = SpringHelperUtil.getApplicationContext().getBean(FlowInfoService.class);
        log.info(" quartz execute {}",jobExecutionContext);
        long flowId = jobExecutionContext.getMergedJobDataMap().getLongValue("flowId");
        scheduleService.schedule(flowId);
    }

}
