package com.yz.kronos.execute;

import com.yz.kronos.model.ExecuteJobInfo;
import lombok.extern.slf4j.Slf4j;


/**
 * 默认的kronos启动器
 * @author shanchong
 * @date 2019-10-19
 **/
@Slf4j
public class DefaultJobExecuteFactory implements JobExecuteFactory {

    ExecuteJobInfo executeJobInfo;
    IsolatedJavaJob isolatedJavaJob;

    @Override
    public void setExecuteJobInfo(ExecuteJobInfo executeJobInfo) {
        this.executeJobInfo = executeJobInfo;
    }

    @Override
    public void setIsolatedJavaJob(IsolatedJavaJob isolatedJavaJob) {
        this.isolatedJavaJob = isolatedJavaJob;
    }

    @Override
    public void init() throws Exception {
        String execId = executeJobInfo.getExecId();
        Integer totalPartitionNums = executeJobInfo.getTotalPartitionNums();
        Integer partitions = executeJobInfo.getPartitions();
        log.info("kronos execute job info :{}", executeJobInfo);
        log.info("kronos execute job start ");
        isolatedJavaJob.execute(partitions,totalPartitionNums,execId);
        log.info("kronos execute job end ");
        System.exit(0);
    }

}
