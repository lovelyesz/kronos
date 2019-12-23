package com.yz.kronos.schedule.repository;

/**
 * 简单的任务执行记录存储器
 * 什么都不做
 * @author shanchong
 * @date 2019-12-23
 **/
public class DefaultJobExecuteRepository implements JobExecuteRepository {
    /**
     * 存储任务执行记录
     *
     * @param flowId 工作流id
     * @param jobId  任务id
     * @return 执行id
     */
    @Override
    public Long insert(Long flowId, Long jobId,Integer shareTotal) {
        return 0L;
    }
}
