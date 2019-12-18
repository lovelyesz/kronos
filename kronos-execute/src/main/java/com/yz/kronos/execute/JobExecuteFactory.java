package com.yz.kronos.execute;

import com.yz.kronos.model.ExecuteJobInfo;

/**
 * 任务执行工厂
 * @author shanchong
 */
public interface JobExecuteFactory {

    /**
     * 任务信息
     * @param executeJobInfo
     */
    void setExecuteJobInfo(ExecuteJobInfo executeJobInfo);

    /**
     * 触发类
     * @param isolatedJavaJob
     */
    void setIsolatedJavaJob(IsolatedJavaJob isolatedJavaJob);

    /**
     * 触发方法
     * @throws Exception
     */
    void init() throws Exception;

}
