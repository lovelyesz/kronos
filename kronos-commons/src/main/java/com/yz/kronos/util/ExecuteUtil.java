package com.yz.kronos.util;

import com.yz.kronos.ExecuteConstant;

/**
 * @author shanchong
 * @date 2019-11-12
 **/
public class ExecuteUtil {

    /**
     * 执行id+工作流id+任务id
     * @param execId
     * @param flowId
     * @param jobId
     * @return
     */
    public static String getExecId(Long execId,Long flowId,Long jobId){
        return execId+":"+flowId+":"+jobId;
    }

    /**
     * 获取同步器的KEY
     * @param execId
     * @param flowId
     * @param jobId
     * @return
     */
    public static String getSynchronizerKey(Long execId,Long flowId,Long jobId){
        return getSynchronizerKey(getExecId(execId, flowId, jobId).replaceAll(":","-"));
    }

    /**
     * 获取同步器的KEY
     * @param execId
     * @return
     */
    public static String getSynchronizerKey(String execId){
        return ExecuteConstant.KRONOS_EXECUTOR_SYNCHRONIZER + execId;
    }


}
