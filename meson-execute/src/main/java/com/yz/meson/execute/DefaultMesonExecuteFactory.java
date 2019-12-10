package com.yz.meson.execute;

import com.yz.meson.model.ExecuteJobInfo;
import lombok.extern.slf4j.Slf4j;


/**
 * 默认的meson启动器
 * @author shanchong
 * @date 2019-10-19
 **/
@Slf4j
public class DefaultMesonExecuteFactory implements MesonExecuteFactory {

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
        log.info("meson execute job info :{}", executeJobInfo);
        log.info("meson execute job start ");
        isolatedJavaJob.execute(partitions,totalPartitionNums,execId);
        log.info("meson execute job end ");
        System.exit(0);
    }

}
