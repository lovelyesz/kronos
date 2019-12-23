package com.yz.kronos.schedule.repository;

/**
 * 任务执行记录
 * @author shanchong
 * @date 2019-12-23
 **/
public interface JobExecuteRepository {

    /**
     * 存储任务执行记录
     * @param flowId 工作流id
     * @param jobId 任务id
     * @param shareTotal
     * @return 执行id
     */
    Long insert(Long flowId ,Long jobId,Integer shareTotal);

}
