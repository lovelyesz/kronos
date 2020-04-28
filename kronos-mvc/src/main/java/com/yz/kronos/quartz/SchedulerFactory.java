package com.yz.kronos.quartz;

import com.yz.kronos.dao.FlowInfoRepository;
import com.yz.kronos.schedule.enu.FlowState;
import com.yz.kronos.schedule.model.FlowInfoModel;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 定时任务自动触发工作流
 * @author shanchong
 * @date 2019-11-13
 **/
//@Component
@Slf4j
public class SchedulerFactory {

    @Autowired
    FlowInfoRepository flowInfoRepository;
    Scheduler scheduler;

    public void init() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        this.scheduler = scheduler;
        scheduler.start();
        //可执行的工作流 list
        List<FlowInfoModel> flowInfoModelList = flowInfoRepository.findByStatus(FlowState.RUNNABLE.code());
        flowInfoModelList.forEach(flowInfoModel-> {
            if (!FlowState.RUNNABLE.code().equals(flowInfoModel.getStatus())){
                return;
            }
            try {
                addJob(flowInfoModel);
            } catch (SchedulerException e1) {
                e1.printStackTrace();
                log.error("load job fail",e1);
            }
        });
        scheduler.start();
    }

    public void addJob(FlowInfoModel flowInfoModel) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(FlowScheduleJob.class)
                .usingJobData("flowId", flowInfoModel.getId())
                .withIdentity(flowInfoModel.getId()+"")
                .build();
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(flowInfoModel.getId()+"")
                .withSchedule(CronScheduleBuilder.cronSchedule(flowInfoModel.getCron()))
                .build();
        scheduler.scheduleJob(jobDetail,cronTrigger);
    }
}
